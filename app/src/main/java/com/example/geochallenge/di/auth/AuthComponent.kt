package com.example.geochallenge.di.auth

import com.example.geochallenge.ui.auth.AuthActivity
import com.example.geochallenge.ui.auth.ForgotPasswordFragment
import com.example.geochallenge.ui.auth.LoginFragment
import com.example.geochallenge.ui.auth.RegistrationFragment
import dagger.BindsInstance
import dagger.Subcomponent


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