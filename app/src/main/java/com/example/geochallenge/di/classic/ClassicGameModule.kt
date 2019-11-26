package com.example.geochallenge.di.classic

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.SinglePlayerLevelProvider
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import com.example.geochallenge.ui.game.classic.ClassicGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class ClassicGameModule(val countTasksForLevel: Int) {

    @Provides
    @ClassicGameScope
    fun provideClassicViewModule(
        activity: GameActivity,
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
    fun provideLevelProvider(tasksService: TaskService): LevelProvider {
        return SinglePlayerLevelProvider(tasksService, countTasksForLevel)
    }

    @Provides
    @ClassicGameScope
    fun provideViewModelFactory(levelProvider: LevelProvider): ClassicGameViewModelFactory {
        return ClassicGameViewModelFactory(levelProvider, countTasksForLevel)

    }

}