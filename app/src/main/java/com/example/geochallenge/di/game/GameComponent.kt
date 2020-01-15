package com.example.geochallenge.di.game


import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.di.records.RecordsComponent
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import dagger.BindsInstance
import dagger.Subcomponent


@GameScope
@Subcomponent(
    modules = [
        GameSubcomponents::class]
)
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance gameInfo: GameInfo,
            @BindsInstance gameMap: GameMap
        ): GameComponent
    }

    fun getGameInfo(): GameInfo
    fun getGameMap(): GameMap
    fun gameActivityComponent(): GameActivityComponent.Factory
    fun recordsComponent(): RecordsComponent.Factory

}