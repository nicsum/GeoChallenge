package com.example.geochallenge.ui.game.classic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.controlers.GameControler

class ClassicGameViewModelFactory(val gameControler: GameControler, val countTasksForLevel: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(GameControler::class.java, Int::class.java)
            .newInstance(gameControler, countTasksForLevel)
//       return ClassicGameViewModel(gameControler)
    }

}