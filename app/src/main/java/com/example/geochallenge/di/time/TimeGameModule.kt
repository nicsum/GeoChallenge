package com.example.geochallenge.di.time

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.SinglePlayerLevelProvider
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameViewModel
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class TimeGameModule(val countTasksForLevel: Int) {

    @Provides
    @TimeGameScope
    fun provideTimeLimitGameViewModule(
        activity: GameActivity,
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
    fun provideLevelProvider(tasksService: TaskService): LevelProvider {
        return SinglePlayerLevelProvider(tasksService, countTasksForLevel)
    }

    @Provides
    @TimeGameScope
    fun provideViewModelFactory(levelProvider: LevelProvider): TimeLimitGameViewModelFactory {
        return TimeLimitGameViewModelFactory(levelProvider)

    }
}