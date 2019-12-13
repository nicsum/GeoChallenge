package com.example.geochallenge.ui.game.classic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import javax.inject.Inject


class ClassicGameViewModelFactory @Inject constructor(
    val gameControler: SinglePlayerGameControler,
    val gameInfo: GameInfo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GameControler::class.java, Int::class.java)
            .newInstance(gameControler, gameInfo.countTaskForLevel)
//       return ClassicGameViewModel(gameControler)
    }

}