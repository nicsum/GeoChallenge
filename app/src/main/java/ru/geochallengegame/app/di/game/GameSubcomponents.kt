package ru.geochallengegame.app.di.game

import dagger.Module
import ru.geochallengegame.app.di.activity.GameActivityComponent
import ru.geochallengegame.app.di.records.RecordsComponent


@Module(
    subcomponents = [
        GameActivityComponent::class,
        RecordsComponent::class
    ]
)
class GameSubcomponents