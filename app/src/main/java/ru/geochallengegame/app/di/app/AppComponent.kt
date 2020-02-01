package ru.geochallengegame.app.di.app


import dagger.BindsInstance
import dagger.Component
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.di.auth.AuthComponent
import ru.geochallengegame.app.di.user.UserComponent
import ru.geochallengegame.app.ui.settings.SettingsFragment
import ru.geochallengegame.app.ui.settings.SettingsManager
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