package com.example.geochallenge.di.multiplayer

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.MultiplayerLevelProvider
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerControler
import com.example.geochallenge.game.multiplayer.MultiplayerApi
import com.example.geochallenge.game.multiplayer.MultiplayerControler
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModel
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModelFactory
import dagger.Module
import dagger.Provides


@Module(includes = arrayOf(MultiplayerApiModule::class))
class MultiplayerGameModule(val countTasksForLevel: Int) {


    @Provides
    @MultiplayerGameScope
    fun provideLevelProvider(): LevelProvider {
        return MultiplayerLevelProvider()
    }

    @Provides
    @MultiplayerGameScope
    fun provideFactory(
        levelProvider: LevelProvider,
        multiplayerControler: MultiplayerControler,
        taskService: TaskService
    ): MultiplayerViewModelFactory {
        return MultiplayerViewModelFactory(
            levelProvider,
            multiplayerControler,
            taskService,
            countTasksForLevel
        )
    }


    @Provides
    @MultiplayerGameScope
    fun provideController(
        taskService: TaskService,
        multiplayerApi: MultiplayerApi
    ): MultiplayerControler {
        return FirebaseMultiplayerControler(taskService, multiplayerApi, countTasksForLevel)
    }

    @Provides
    @MultiplayerGameScope
    fun provideMultiplayerViewMode(
        activity: GameActivity,
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