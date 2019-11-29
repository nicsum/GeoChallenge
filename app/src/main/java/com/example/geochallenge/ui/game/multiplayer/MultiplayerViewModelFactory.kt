package com.example.geochallenge.ui.game.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.multiplayer.MultiplayerControler

class MultiplayerViewModelFactory(
    val gameControler: GameControler,
    val multiplayerControler: MultiplayerControler,
    val geochallengeService: GeochallengeService,
    val countTasksForLevel: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GameControler::class.java,
            MultiplayerControler::class.java,
            GeochallengeService::class.java,
            Int::class.java
        ).newInstance(gameControler, multiplayerControler, geochallengeService, countTasksForLevel)
    }
}