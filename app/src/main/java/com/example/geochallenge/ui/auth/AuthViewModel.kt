package com.example.geochallenge.ui.auth

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel(val firebaseAuth: FirebaseAuth, val db: FirebaseFirestore) : ViewModel() {

    var currentUsername = ""
    var currentEmail = ""
    var currentPassword = ""

    val usernameError =
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_USERNAME_IS_EMPTY }
    val passwordError =
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_PASSWORD_IS_EMPTY }
    val emailError =
        MutableLiveData<AuthErrors>().also { it.value = AuthErrors.FIELD_EMAIL_IS_EMPTY }
    val loginError = MutableLiveData<AuthErrors>().also { it.value = AuthErrors.NONE }

    val isSignIn = MutableLiveData<Boolean>().also { it.postValue(false) }
    val isPasswordReset = MutableLiveData<Boolean>().also { it.postValue(false) }

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

        //check username
        db.collection("usernames")
            .document(currentUsername)
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    if (it.id == currentUsername) this.usernameError.postValue(AuthErrors.USERNAME_ALREADY_IN_USE)
                } else {
                    this.usernameError.postValue(AuthErrors.NONE)
                    createUserWithEmailAndPasswordAndUsername()
                }
            }.addOnFailureListener {
                Log.e("AuthViewModel", it.message)
            }
    }

    fun sendPasswordResetEmail() {
        if (!forgotPasswordFieldsAreCorrect()) return

        firebaseAuth.sendPasswordResetEmail(currentEmail)
            .addOnSuccessListener {
                isPasswordReset.postValue(true)
            }
            .addOnFailureListener {
                resolveFirebaseError(it as FirebaseException)
            }

    }

    private fun createUserWithEmailAndPasswordAndUsername() {
        firebaseAuth.createUserWithEmailAndPassword(currentEmail, currentPassword)
            .addOnCompleteListener {
                if (it.isSuccessful && it.result?.user != null) {
                    addUser(currentUsername, it.result?.user?.uid!!)
                } else {
                    resolveFirebaseError(it.exception as FirebaseException)
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

    fun login() {
        loginError.postValue(AuthErrors.NONE)
        if (!loginFieldsAreCorrect()) return
        firebaseAuth.signInWithEmailAndPassword(currentEmail, currentPassword)
            .addOnSuccessListener {
                isSignIn.postValue(true)
            }
            .addOnFailureListener {
                Log.e("AuthViewModel", it.message.toString())
                resolveFirebaseError(it as FirebaseException)
            }
    }

    private fun addUser(username: String, uid: String) {
        firebaseAuth.currentUser?.updateProfile(
            UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
        )
        db.collection("usernames")
            .document(username).set(mapOf("uid" to uid))
            .addOnSuccessListener {
                isSignIn.postValue(true)
            }
    }

    private fun resolveFirebaseError(e: FirebaseException) {
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
            is FirebaseAuthInvalidUserException -> loginError.postValue(AuthErrors.INVALID_USER)
            is FirebaseTooManyRequestsException -> loginError.postValue(AuthErrors.TO_MANY_REQUESTS) //TODO вынести ошибку не под email
        }
    }

    fun clearField() {
        currentUsername = ""
        currentEmail = ""
        currentPassword = ""
        isPasswordReset.value = false
        usernameError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        passwordError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        emailError.value = AuthErrors.FIELD_USERNAME_IS_EMPTY
        loginError.value = AuthErrors.NONE
    }

}