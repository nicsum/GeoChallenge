package com.example.geochallenge.di.multiplayer

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.MultiplayerGameControler
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerControler
import com.example.geochallenge.game.multiplayer.MultiplayerApi
import com.example.geochallenge.game.multiplayer.MultiplayerControler
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModel
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModelFactory
import dagger.Module
import dagger.Provides


@Module(includes = arrayOf(MultiplayerApiModule::class))
class MultiplayerGameModule(val countTasksForLevel: Int) {


    @Provides
    @MultiplayerGameScope
    fun provideLevelProvider(): GameControler {
        return MultiplayerGameControler()
    }

    @Provides
    @MultiplayerGameScope
    fun provideFactory(
        gameControler: GameControler,
        multiplayerControler: MultiplayerControler,
        geochallengeService: GeochallengeService
    ): MultiplayerViewModelFactory {
        return MultiplayerViewModelFactory(
            gameControler,
            multiplayerControler,
            geochallengeService,
            countTasksForLevel
        )
    }


    @Provides
    @MultiplayerGameScope
    fun provideController(
        geochallengeService: GeochallengeService,
        multiplayerApi: MultiplayerApi
    ): MultiplayerControler {
        return FirebaseMultiplayerControler(geochallengeService, multiplayerApi, countTasksForLevel)
    }

    @Provides
    @MultiplayerGameScope
    fun provideMultiplayerViewMode(
        activity: BaseGameMapActivity,
        factory: MultiplayerViewModelFactory
    ): MultiplayerViewModel {
        return ViewModelProviders.of(activity, factory).get(MultiplayerViewModel::class.java)
    }

    @Provides
    @MultiplayerGameScope
    fun provideViewMode(viewModel: MultiplayerViewModel): BaseGameViewModel {
        return viewModel
    }


}