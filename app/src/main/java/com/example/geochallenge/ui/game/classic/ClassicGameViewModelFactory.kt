package com.example.geochallenge.ui.game.classic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.levels.LevelProvider

class ClassicGameViewModelFactory(val levelProvider: LevelProvider, val countTasksForLevel: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LevelProvider::class.java, Int::class.java)
            .newInstance(levelProvider, countTasksForLevel)
//       return ClassicGameViewModel(levelProvider)
    }

}