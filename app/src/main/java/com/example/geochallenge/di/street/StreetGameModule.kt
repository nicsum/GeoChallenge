package com.example.geochallenge.di.street

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.SinglePlayerLevelProvider
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.street.StreetGameViewModel
import com.example.geochallenge.ui.game.street.StreetGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class StreetGameModule(val countTasksForLevel: Int) {

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
    fun provideLevelProvider(tasksService: TaskService): LevelProvider {
        return SinglePlayerLevelProvider(tasksService, countTasksForLevel)
    }

    @Provides
    @StreetGameScope
    fun provideViewModelFactory(levelProvider: LevelProvider): StreetGameViewModelFactory {
        return StreetGameViewModelFactory(levelProvider)

    }

    @Provides
    @StreetGameScope
    fun provideViewModule(viewModel: StreetGameViewModel): BaseGameViewModel {
        return viewModel
    }


}