package ru.geochallengegame.app.ui.game.time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.game.controlers.SinglePlayerGameController
import javax.inject.Inject

class TimeLimitGameViewModelFactory @Inject constructor(
    private val gameControler: SinglePlayerGameController,
    val gameMap: GameMap,
    val gameInfo: GameInfo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(GameController::class.java, GameMap::class.java, GameInfo::class.java)
            .newInstance(gameControler, gameMap, gameInfo)
    }
}