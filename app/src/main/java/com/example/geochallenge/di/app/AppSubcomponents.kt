package com.example.geochallenge.di.app

import com.example.geochallenge.di.auth.AuthComponent
import com.example.geochallenge.di.user.UserComponent
import dagger.Module


@Module(
    subcomponents = [
        UserComponent::class,
        AuthComponent::class
    ]
)
class AppSubcomponents