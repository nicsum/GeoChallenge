package com.example.geochallenge.ui.game.classic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import javax.inject.Inject


class ClassicGameViewModelFactory @Inject constructor(
    val gameControler: SinglePlayerGameControler,
    val gameMap: GameMap,
    val gameInfo: GameInfo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GameControler::class.java,
            GameMap::class.java,
            GameInfo::class.java
        )
            .newInstance(gameControler, gameMap, gameInfo)
//       return ClassicGameViewModel(gameControler)
    }

}