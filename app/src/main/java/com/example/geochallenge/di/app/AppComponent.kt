package com.example.geochallenge.di.app


import com.example.geochallenge.AppDelegate
import com.example.geochallenge.di.auth.AuthComponent
import com.example.geochallenge.di.user.UserComponent
import com.example.geochallenge.ui.settings.SettingsFragment
import com.example.geochallenge.ui.settings.SettingsManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        AppSubcomponents::class,
        FirebaseModule::class,
        GoogleApiModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appDelegate: AppDelegate): AppComponent
    }

    fun inject(fragment: SettingsFragment)

    fun getSettingsManager(): SettingsManager

    fun userComponent(): UserComponent.Factory
    fun authComponent(): AuthComponent.Factory

}