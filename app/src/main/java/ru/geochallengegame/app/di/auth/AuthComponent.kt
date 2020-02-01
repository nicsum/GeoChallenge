package ru.geochallengegame.app.di.auth

import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.ui.auth.AuthActivity
import ru.geochallengegame.app.ui.auth.ForgotPasswordFragment
import ru.geochallengegame.app.ui.auth.LoginFragment
import ru.geochallengegame.app.ui.auth.RegistrationFragment


@AuthScope
@Subcomponent(
    modules = [
        AuthModule::class
    ]
)
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AuthActivity): AuthComponent
    }

    fun inject(activity: AuthActivity)
    fun inject(fragment: LoginFragment)
    fun inject(activity: RegistrationFragment)
    fun inject(activity: ForgotPasswordFragment)
}