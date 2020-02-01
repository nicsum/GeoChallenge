package ru.geochallengegame.app.di.auth


import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.geochallengegame.app.ui.auth.AuthActivity
import ru.geochallengegame.app.ui.auth.AuthViewModel
import ru.geochallengegame.app.ui.auth.AuthViewModelFactory


@Module
class AuthModule {

    @Provides
    fun provideAuthViewModel(
        activity: AuthActivity,
        factory: AuthViewModelFactory
    ): AuthViewModel {

        return ViewModelProvider(activity, factory).get(AuthViewModel::class.java)

    }
}