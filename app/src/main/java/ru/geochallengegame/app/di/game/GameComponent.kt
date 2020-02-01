package ru.geochallengegame.app.di.game


import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.di.activity.GameActivityComponent
import ru.geochallengegame.app.di.records.RecordsComponent
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap


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