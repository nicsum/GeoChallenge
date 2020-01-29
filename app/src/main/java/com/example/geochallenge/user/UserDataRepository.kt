package com.example.geochallenge.user

import com.example.geochallenge.di.user.LoggedUserScope
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

@LoggedUserScope
class UserDataRepository @Inject constructor(private val firebaseUser: FirebaseUser) {


    val username: String
        get() = firebaseUser.displayName!!

    val uid: String
        get() = firebaseUser.uid

}