package com.example.geochallenge.ui.game.timelimit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.levels.LevelProvider

class TimeLimitGameViewModelFactory(val levelProvider: LevelProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LevelProvider::class.java).newInstance(levelProvider)
    }
}