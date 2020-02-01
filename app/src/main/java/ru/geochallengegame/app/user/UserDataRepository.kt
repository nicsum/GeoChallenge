package ru.geochallengegame.app.user


import com.google.firebase.auth.FirebaseUser
import ru.geochallengegame.app.di.user.LoggedUserScope
import javax.inject.Inject

@LoggedUserScope
class UserDataRepository @Inject constructor(private val firebaseUser: FirebaseUser) {


    val username: String
        get() = firebaseUser.displayName!!

    val uid: String
        get() = firebaseUser.uid

}