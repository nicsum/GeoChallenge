package ru.geochallengegame.app

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

import com.google.firebase.auth.FirebaseUser
import ru.geochallengegame.app.di.app.AppComponent
import ru.geochallengegame.app.di.app.DaggerAppComponent
import ru.geochallengegame.app.di.game.GameComponent
import ru.geochallengegame.app.di.user.UserComponent
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap

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