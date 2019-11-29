package com.example.geochallenge.di.time

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameViewModel
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class TimeGameModule {

    @Provides
    @TimeGameScope
    fun provideTimeLimitGameViewModule(
        activity: BaseGameMapActivity,
        factory: TimeLimitGameViewModelFactory
    ): TimeLimitGameViewModel {
        return ViewModelProviders.of(activity, factory).get(TimeLimitGameViewModel::class.java)
    }

    @Provides
    @TimeGameScope
    fun provideViewModule(viewModel: TimeLimitGameViewModel): BaseGameViewModel {
        return viewModel
    }

    @Provides
    @TimeGameScope
    fun provideLevelProvider(
        tasksService: GeochallengeService,
        gameInfo: GameInfo,
        userId: String
    ): GameControler {
        return SinglePlayerGameControler(tasksService, gameInfo, userId)
    }

    @Provides
    @TimeGameScope
    fun provideViewModelFactory(gameControler: GameControler): TimeLimitGameViewModelFactory {
        return TimeLimitGameViewModelFactory(gameControler)

    }
}