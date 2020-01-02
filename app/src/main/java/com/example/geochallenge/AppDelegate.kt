package com.example.geochallenge


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.geochallenge.di.app.AppComponent
import com.example.geochallenge.di.app.DaggerAppComponent
import com.example.geochallenge.di.game.GameComponent
import com.example.geochallenge.di.user.UserComponent
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.google.firebase.auth.FirebaseUser

class AppDelegate : MultiDexApplication()  {

    lateinit var appComponent: AppComponent

    var userComponent: UserComponent? = null
        private set

    var gameComponent: GameComponent? = null
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

    //user component
    fun createUserComponent(firebaseUser: FirebaseUser): UserComponent? {
        userComponent = appComponent.userComponent().create(firebaseUser)
        return userComponent
    }

    fun destroyUserComponent() {
        userComponent = null
    }

    //game component

    fun createGameComponent(gameInfo: GameInfo, gameMap: GameMap): GameComponent? {
        gameComponent = userComponent?.gameComponent()?.create(gameInfo, gameMap)
        return gameComponent
    }

    fun destroyGameComponent() {
        gameComponent = null
    }

}