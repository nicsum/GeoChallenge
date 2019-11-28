package com.example.geochallenge.ui.game.street

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.game.levels.LevelProvider

class StreetGameViewModelFactory(val levelProvider: LevelProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LevelProvider::class.java)
            .newInstance(levelProvider)
    }
}