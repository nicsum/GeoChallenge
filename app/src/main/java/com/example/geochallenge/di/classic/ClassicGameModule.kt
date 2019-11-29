package com.example.geochallenge.di.classic

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import com.example.geochallenge.ui.game.classic.ClassicGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class ClassicGameModule {

    @Provides
    @ClassicGameScope
    fun provideClassicViewModule(
        activity: BaseGameMapActivity,
        factory: ClassicGameViewModelFactory
    ): ClassicGameViewModel {
        return ViewModelProviders.of(activity, factory).get(ClassicGameViewModel::class.java)
    }

    @Provides
    @ClassicGameScope
    fun provideViewModule(viewModel: ClassicGameViewModel): BaseGameViewModel {
        return viewModel
    }

    @Provides
    @ClassicGameScope
    fun provideLevelProvider(
        tasksService: GeochallengeService,
        gameInfo: GameInfo,
        userId: String
    ): GameControler {
        return SinglePlayerGameControler(tasksService, gameInfo, userId)
    }

    @Provides
    @ClassicGameScope
    fun provideViewModelFactory(
        gameControler: GameControler,
        gameInfo: GameInfo
    ): ClassicGameViewModelFactory {
        return ClassicGameViewModelFactory(gameControler, gameInfo.countTaskForLevel)

    }

}