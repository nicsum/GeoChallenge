package com.example.geochallenge


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.geochallenge.di.activity.ActivityComponent
import com.example.geochallenge.di.activity.GameActivityModule
import com.example.geochallenge.di.app.AppComponent
import com.example.geochallenge.di.app.AppModule
import com.example.geochallenge.di.app.DaggerAppComponent
import com.example.geochallenge.ui.game.GameActivity
import com.google.android.gms.maps.model.LatLng

class AppDelegate : MultiDexApplication()  {


    lateinit var appComponent: AppComponent

    var gameActivityComponent: ActivityComponent? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getGameActivityComponent(
        activity: GameActivity,
        gameType: String,
        startLocation: LatLng
    ): ActivityComponent {
        val component = gameActivityComponent ?: appComponent.provideActivityComponent(
            GameActivityModule(activity, gameType, startLocation)
        )
        gameActivityComponent = component
        return component
    }

    fun dropGameActivityComponent() {
        gameActivityComponent = null
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

    }

}