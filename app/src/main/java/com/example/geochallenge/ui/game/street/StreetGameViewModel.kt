package com.example.geochallenge.ui.game.street


import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.ui.game.BaseGameViewModel
import io.reactivex.Completable
import io.reactivex.Single

class StreetGameViewModel(val levelProvider: LevelProvider) : BaseGameViewModel() {

    override fun getNextTask(): Single<CityTask> {
        return levelProvider.getNextTask()
    }

    override fun prepareNewLevel(newLevel: Int): Completable {

        return levelProvider.prepareForLevel(newLevel)
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return levelProvider.haveTaskForCurrentLevel()
    }

}