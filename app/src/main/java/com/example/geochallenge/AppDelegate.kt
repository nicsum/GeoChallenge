package com.example.geochallenge


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.geochallenge.di.app.AppComponent
import com.example.geochallenge.di.app.DaggerAppComponent
import com.example.geochallenge.di.user.UserComponent
import com.google.firebase.auth.FirebaseUser

class AppDelegate : MultiDexApplication()  {

    lateinit var appComponent: AppComponent

    var userComponent: UserComponent? = null
        private set

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    fun createUserComponent(firebaseUser: FirebaseUser): UserComponent? {
        userComponent = appComponent.userComponent().create(firebaseUser)
        return userComponent
    }

    fun destroyUserComponent() {
        userComponent = null
    }

}