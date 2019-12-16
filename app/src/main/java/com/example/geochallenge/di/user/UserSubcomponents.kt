package com.example.geochallenge.di.user

import com.example.geochallenge.di.game.GameComponent
import com.example.geochallenge.di.menu.MenuComponent
import dagger.Module


@Module(
    subcomponents = [
        GameComponent::class,
        MenuComponent::class
    ]
)
class UserSubcomponents