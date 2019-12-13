package com.example.geochallenge.di.user

import com.example.geochallenge.di.game.GameComponent
import dagger.Module


@Module(
    subcomponents = [
        GameComponent::class
    ]
)
class UserSubcomponents