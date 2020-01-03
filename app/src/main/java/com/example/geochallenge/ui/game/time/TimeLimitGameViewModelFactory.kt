package com.example.geochallenge.ui.game.time

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import javax.inject.Inject

class TimeLimitGameViewModelFactory @Inject constructor(
    val gameControler: SinglePlayerGameControler,
    val gameInfo: GameInfo
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(GameControler::class.java, GameInfo::class.java)
            .newInstance(gameControler, gameInfo)
    }
}