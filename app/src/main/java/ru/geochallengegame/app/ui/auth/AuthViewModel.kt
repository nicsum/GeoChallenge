package ru.geochallengegame.app.ui.auth

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

class AuthViewModel(private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore) :
    ViewModel() {

    private var currentUsername = ""
    private var currentEmail = ""
    private var currentPassword = ""

    val usernameError =
        MutableLiveData<AuthErrors>(AuthErrors.NONE)
    val passwordError =
        MutableLiveData<AuthErrors>(AuthErrors.NONE)
    val emailError =
        MutableLiveData<AuthErrors>(AuthErrors.NONE)
    val authError = MutableLiveData<AuthErrors>(AuthErrors.NONE)

    val isSignIn = MutableLiveData<Boolean>(false)
    val isPasswordReset = MutableLiveData<Boolean>(false)
    val loadingIsVisible = MutableLiveData<Boolean>(false)

    val loginScreen = MutableLiveData<Boolean>(true)
    val registrationScreen = MutableLiveData<Boolean>(false)
    val forgotPasswordScreen = MutableLiveData<Boolean>(false)

    init {
        loginScreen.observeForever {
            if (it) {
                registrationScreen.postValue(false)
                forgotPasswordScreen.postValue(false)
            }
        }

        registrationScreen.observeForever {
            if (it) {
                loginScreen.postValue(false)
                forgotPasswordScreen.postValue(false)
            }
        }

        forgotPasswordScreen.observeForever {
            if (it) {
                loginScreen.postValue(false)
                registrationScreen.postValue(false)
            }
        }
    }

    fun enterUsername(username: String) {
        currentUsername = username
        validateUsername(currentUsername)

    }

    private fun validateUsername(username: String): Boolean {
        if (username.isEmpty()) {
            usernameError.postValue(AuthErrors.FIELD_USERNAME_IS_EMPTY)
            return false
        }
        usernameError.postValue(AuthErrors.NONE)
        return true
    }

    fun enterPassword(password: String) {
        currentPassword = password
        validatePassword(currentPassword)
    }

    private fun validatePassword(password: String): Boolean {
        if (password.isEmpty()) {
            passwordError.postValue(AuthErrors.FIELD_PASSWORD_IS_EMPTY)
            return false
        }
        passwordError.postValue(AuthErrors.NONE)
        return true
    }

    fun enterEmail(email: String) {
        currentEmail = email
        validateEmail(currentEmail)

    }

    private fun validateEmail(email: String): Boolean {
        if (currentEmail.isEmpty()) {
            emailError.postValue(AuthErrors.FIELD_EMAIL_IS_EMPTY)
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.postValue(AuthErrors.NOT_CORRECT_EMAIL)
            return false
        }
        emailError.postValue(AuthErrors.NONE)
        return true
    }

    fun registration() {
        if (!registrationFieldsAreCorrect()) return

        loadingIsVisible.postValue(true)
        db.collection("usernames")
            .document(currentUsername)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    loadingIsVisible.postValue(false)
                    if (it.id == currentUsername)
                        this.usernameError.postValue(AuthErrors.USERNAME_ALREADY_IN_USE)
                } else {
                    this.usernameError.postValue(AuthErrors.NONE)
                    createUserWithEmailAndPasswordAndUsername()
                }
            }.addOnFailureListener {
                resolveError(it)
                loadingIsVisible.postValue(false)
            }
    }

    fun authWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        loadingIsVisible.postValue(true)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                loadingIsVisible.postValue(false)
                if (task.isSuccessful) {
                    isSignIn.postValue(true)
                } else {
                    resolveError(task.exception)
                }
            }
    }


    fun login() {
        if (!loginFieldsAreCorrect()) return
        loadingIsVisible.postValue(true)
        firebaseAuth.signInWithEmailAndPassword(currentEmail, currentPassword)
            .addOnSuccessListener {
                isSignIn.postValue(true)
            }
            .addOnFailureListener {
                resolveError(it)
            }.addOnCompleteListener {
                loadingIsVisible.postValue(false)
            }
    }
    fun sendPasswordResetEmail() {
        if (!forgotPasswordFieldsAreCorrect()) return

        loadingIsVisible.postValue(true)
        firebaseAuth.sendPasswordResetEmail(currentEmail)
            .addOnSuccessListener {
                isPasswordReset.postValue(true)
            }
            .addOnFailureListener {
                resolveError(it)
            }.addOnCompleteListener {
                loadingIsVisible.postValue(false)
            }

    }

    private fun createUserWithEmailAndPasswordAndUsername() {
        firebaseAuth.createUserWithEmailAndPassword(currentEmail, currentPassword)
            .addOnCompleteListener {
                if (it.isSuccessful && it.result?.user != null) {
                    addUser(currentUsername, it.result?.user?.uid!!)
                } else {
                    loadingIsVisible.postValue(false)
                    resolveError(it.exception)
                }
            }
    }

    private fun registrationFieldsAreCorrect(): Boolean {
        return validateUsername(currentUsername) &&
                validateEmail(currentEmail) &&
                validatePassword(currentPassword)
    }

    private fun loginFieldsAreCorrect(): Boolean {
        return validateEmail(currentEmail) &&
                validatePassword(currentPassword)
    }

    private fun forgotPasswordFieldsAreCorrect(): Boolean {
        return validateEmail(currentEmail)

    }


    private fun addUser(username: String, uid: String) {

        db.collection("usernames")
            .document(username)
            .set(mapOf("uid" to uid))
            .addOnSuccessListener {
                isSignIn.postValue(true)
                firebaseAuth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build()
                )
            }.addOnFailureListener {
                firebaseAuth.currentUser?.delete()
                resolveError(it)
            }.addOnCompleteListener {
                loadingIsVisible.postValue(false)
            }
    }


    private fun resolveError(e: Exception?) {

        if (e == null) return
        when (e) {
            is FirebaseAuthUserCollisionException -> {
                if (e.errorCode == "ERROR_EMAIL_ALREADY_IN_USE") {
                    emailError.postValue(AuthErrors.EMAIL_ALREADY_IN_USE)
                }
            }
            is FirebaseAuthWeakPasswordException ->
                passwordError.postValue(AuthErrors.SHORT_PASSWORD)

            is FirebaseAuthInvalidCredentialsException -> {
                if (e.errorCode == "ERROR_WRONG_PASSWORD") {
                    passwordError.postValue(AuthErrors.WRONG_PASSWORD)
                }
            }
            is FirebaseFirestoreException ->
                if (e.code == FirebaseFirestoreException.Code.UNAVAILABLE) {
                    this.authError.postValue(AuthErrors.CONNECTION_FAILED)
                }
            is FirebaseAuthInvalidUserException -> authError.postValue(AuthErrors.INVALID_USER)
            is FirebaseTooManyRequestsException -> authError.postValue(AuthErrors.TO_MANY_REQUESTS)
            else -> authError.postValue(AuthErrors.ANY)
        }
    }

    fun showRegistrationScreen() {
        clearFields()
        registrationScreen.postValue(true)
    }

    fun showLoginScreen() {
        clearFields()
        loginScreen.postValue(true)
    }

    fun showForgotPasswordScreen() {
        clearFields()
        forgotPasswordScreen.postValue(true)
    }

    fun iKnowAboutAuthError() {
        authError.postValue(AuthErrors.NONE)
    }

    private fun clearFields() {
        currentUsername = ""
        currentEmail = ""
        currentPassword = ""
        isPasswordReset.value = false
        usernameError.value =
            AuthErrors.NONE
        passwordError.value =
            AuthErrors.NONE
        emailError.value =
            AuthErrors.NONE
        authError.value = AuthErrors.NONE
    }

}