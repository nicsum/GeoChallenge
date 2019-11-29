package com.example.geochallenge.ui.game.timelimit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.controlers.GameControler

class TimeLimitGameViewModelFactory(val gameControler: GameControler) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GameControler::class.java).newInstance(gameControler)
    }
}