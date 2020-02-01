package ru.geochallengegame.app.di.user

import dagger.Module
import ru.geochallengegame.app.di.game.GameComponent
import ru.geochallengegame.app.di.menu.MenuComponent


@Module(
    subcomponents = [
        GameComponent::class,
        MenuComponent::class
    ]
)
class UserSubcomponents