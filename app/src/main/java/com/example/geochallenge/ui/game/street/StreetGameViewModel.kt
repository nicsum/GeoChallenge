package com.example.geochallenge.ui.game.street


import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.ui.game.BaseGameViewModel
import io.reactivex.Single

class StreetGameViewModel(val gameControler: GameControler) : BaseGameViewModel() {

    override fun getNextTask(): Single<CityTask> {
        return gameControler.getNextTask()
    }

    override fun prepareNewLevel(newLevel: Int): Single<Int> {

        return gameControler.prepareForLevel(newLevel)
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameControler.haveTaskForCurrentLevel()
    }

}