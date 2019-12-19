package com.example.geochallenge.di.auth

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.ui.auth.AuthActivity
import com.example.geochallenge.ui.auth.AuthViewModel
import com.example.geochallenge.ui.auth.AuthViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class AuthModule {

    @Provides
    fun provideAuthViewModel(
        activity: AuthActivity,
        factory: AuthViewModelFactory
    ): AuthViewModel {
        return ViewModelProviders.of(activity, factory).get(AuthViewModel::class.java)

    }
}