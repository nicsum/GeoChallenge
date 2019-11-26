package com.example.geochallenge.ui.game.multiplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.multiplayer.MultiplayerControler

class MultiplayerViewModelFactory(
    val levelProvider: LevelProvider,
    val multiplayerControler: MultiplayerControler,
    val taskService: TaskService,
    val countTasksForLevel: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            LevelProvider::class.java,
            MultiplayerControler::class.java,
            TaskService::class.java,
            Int::class.java
        ).newInstance(levelProvider, multiplayerControler, taskService, countTasksForLevel)
    }
}