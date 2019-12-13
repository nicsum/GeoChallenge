package com.example.geochallenge.di.game

import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.di.records.RecordsComponent
import dagger.Module


@Module(
    subcomponents = [
        GameActivityComponent::class,
        RecordsComponent::class
    ]
)
class GameSubcomponents