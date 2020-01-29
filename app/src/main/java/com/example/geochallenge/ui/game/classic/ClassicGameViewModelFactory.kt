package com.example.geochallenge.ui.game.classic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.controlers.GameController
import com.example.geochallenge.game.controlers.SinglePlayerGameController
import javax.inject.Inject


class ClassicGameViewModelFactory @Inject constructor(
    private val gameController: SinglePlayerGameController,
    private val gameMap: GameMap,
    private val gameInfo: GameInfo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GameController::class.java,
            GameMap::class.java,
            GameInfo::class.java
        )
            .newInstance(gameController, gameMap, gameInfo)

    }

}