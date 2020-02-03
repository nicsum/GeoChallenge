package ru.geochallengegame.app.ui.game.hundred

import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.ui.game.classic.ClassicGameViewModel

class HungredGameViewModel(
    gameController: GameController,
    gameMap: GameMap,
    gameInfo: GameInfo
) : ClassicGameViewModel(gameController, gameMap, gameInfo) {

    companion object {
        const val FATAL_DISTANCE = 100
    }


    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        super.clickedPosition(latitude, longitude, distance)
        if (distance > FATAL_DISTANCE) finishGame()
    }
}