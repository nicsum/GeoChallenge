package ru.geochallengegame.app.di.app

import dagger.Module
import ru.geochallengegame.app.di.auth.AuthComponent
import ru.geochallengegame.app.di.user.UserComponent


@Module(
    subcomponents = [
        UserComponent::class,
        AuthComponent::class
    ]
)
class AppSubcomponents