package com.example.geochallenge.di.street

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.controlers.SinglePlayerGameControler
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.street.StreetGameViewModel
import com.example.geochallenge.ui.game.street.StreetGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class StreetGameModule {

    @Provides
    @StreetGameScope
    fun provideClassicViewModule(
        activity: BaseGameMapActivity,
        factory: StreetGameViewModelFactory
    ): StreetGameViewModel {
        return ViewModelProviders.of(activity, factory).get(StreetGameViewModel::class.java)

    }

    @Provides
    @StreetGameScope
    fun provideLevelProvider(
        tasksService: GeochallengeService,
        gameInfo: GameInfo,
        userId: String
    ): GameControler {
        return SinglePlayerGameControler(tasksService, gameInfo, userId)
    }

    @Provides
    @StreetGameScope
    fun provideViewModelFactory(gameControler: GameControler): StreetGameViewModelFactory {
        return StreetGameViewModelFactory(gameControler)

    }

    @Provides
    @StreetGameScope
    fun provideViewModule(viewModel: StreetGameViewModel): BaseGameViewModel {
        return viewModel
    }


}