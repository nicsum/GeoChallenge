package com.example.geochallenge.ui.auth

import android.util.Log
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
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_USERNAME_IS_EMPTY }
    val passwordError =
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_PASSWORD_IS_EMPTY }
    val emailError =
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_EMAIL_IS_EMPTY }

    val authError = MutableLiveData<AuthErrors>().also { it.value = AuthErrors.NONE }

    val isSignIn = MutableLiveData<Boolean>().also { it.postValue(false) }
    val isPasswordReset = MutableLiveData<Boolean>().also { it.postValue(false) }
    val loadingIsVisible = MutableLiveData<Boolean>().also { it.postValue(false) }

    fun enterUsername(username: String) {
        currentUsername = username
        if (username.isEmpty()) {
            usernameError.postValue(AuthErrors.FIELD_USERNAME_IS_EMPTY)
            return
        }
        usernameError.postValue(AuthErrors.NONE)
    }

    fun enterPassword(password: String) {
        currentPassword = password
        if (password.isEmpty()) {
            passwordError.postValue(AuthErrors.FIELD_PASSWORD_IS_EMPTY)
            return
        }
        passwordError.postValue(AuthErrors.NONE)
    }

    fun enterEmail(email: String) {
        currentEmail = email

        if (currentEmail.isEmpty()) {
            emailError.postValue(AuthErrors.FIELD_PASSWORD_IS_EMPTY)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError.postValue(AuthErrors.NOT_CORRECT_EMAIL)
            return
        }
        emailError.postValue(AuthErrors.NONE)
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
        loadingIsVisible.postValue(true)
        authError.postValue(AuthErrors.NONE)
        if (!loginFieldsAreCorrect()) return
        firebaseAuth.signInWithEmailAndPassword(currentEmail, currentPassword)
            .addOnSuccessListener {
                isSignIn.postValue(true)
            }
            .addOnFailureListener {
                Log.e("AuthViewModel", it.message.toString())
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
                    Log.e("AuthViewModel", it.exception?.message.toString())
                }
            }
    }

    private fun registrationFieldsAreCorrect(): Boolean {
        return passwordError.value == AuthErrors.NONE &&
                emailError.value == AuthErrors.NONE &&
                usernameError.value == AuthErrors.NONE
    }

    private fun loginFieldsAreCorrect(): Boolean {
        return passwordError.value == AuthErrors.NONE &&
                emailError.value == AuthErrors.NONE
    }

    private fun forgotPasswordFieldsAreCorrect(): Boolean {
        return emailError.value == AuthErrors.NONE

    }


    private fun addUser(username: String, uid: String) {

        db.collection("usernames")
            .document(username).set(mapOf("uid" to uid))
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

    fun iKnowAboutAuthError() {
        authError.postValue(AuthErrors.NONE)
    }

    fun clearField() {
        currentUsername = ""
        currentEmail = ""
        currentPassword = ""
        isPasswordReset.value = false
        usernameError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        passwordError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        emailError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        authError.value = AuthErrors.NONE
    }

}