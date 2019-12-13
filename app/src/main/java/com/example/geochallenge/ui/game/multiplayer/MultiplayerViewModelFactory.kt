package com.example.geochallenge.ui.game.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.MultiplayerGameControler
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerDispatcher
import com.example.geochallenge.game.multiplayer.MultiplayerDispatcher
import javax.inject.Inject

class MultiplayerViewModelFactory @Inject constructor(
    val gameControler: MultiplayerGameControler,
    val multiplayerDispatcher: FirebaseMultiplayerDispatcher,
    val geochallengeService: GeochallengeService,
    val gameInfo: GameInfo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GameControler::class.java,
            MultiplayerDispatcher::class.java,
            GeochallengeService::class.java,
            Int::class.java
        ).newInstance(
            gameControler,
            multiplayerDispatcher,
            geochallengeService,
            gameInfo.countTaskForLevel
        )
    }
}