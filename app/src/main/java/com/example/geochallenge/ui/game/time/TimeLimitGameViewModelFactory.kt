package com.example.geochallenge.ui.game.time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.controlers.GameController
import com.example.geochallenge.game.controlers.SinglePlayerGameController
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