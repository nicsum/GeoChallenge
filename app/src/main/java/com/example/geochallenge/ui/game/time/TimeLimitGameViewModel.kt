package com.example.geochallenge.ui.game.time

import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameError
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimeLimitGameViewModel(
    val gameControler: GameControler,
    val gameInfo: GameInfo
) : BaseGameViewModel() {

    companion object {
        const val COUNT_TIMER : Long = 30
    }

    val stillHaveTime = MutableLiveData<Long>()
    var stillHaveTimeLong =
        COUNT_TIMER
    var timerDisposable: Disposable? = null

    override fun newGame() {
        super.newGame()
        startTimerFromCount(COUNT_TIMER)

    }

     override fun clickedPosition(latitude: Double, longitude: Double, distance: Int) {
         super.clickedPosition(latitude, longitude, distance)

         //calculate time
         val timeBonus = getExtraTime(distance)
         val resultTime = stillHaveTimeLong + timeBonus

         if (resultTime <= 0) finishGame()
         else startTimerFromCount(resultTime)

     }

     override fun levelFinished() {
         super.levelFinished()
         nextLevel()
     }


    fun startTimerFromCount(count : Long ){
        timerDisposable?.dispose()

        timerDisposable = Observable
            .intervalRange(1, count + 1,1,1, TimeUnit.SECONDS)
            .doOnComplete(this::finishGame)
            .map { count + 1 - it  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                stillHaveTime.postValue( it)
                stillHaveTimeLong = it
            }
    }


     private fun getExtraTime(distance: Int) = when {

         distance <= 100 -> 9
         distance <= 200 -> 8
         distance <= 300 -> 7
         distance <= 400 -> 6
         distance <= 500 -> 5
         distance <= 600 -> 0
         distance <= 700 -> -2
         distance <= 800 -> -3
         distance <= 900 -> -4
         distance <= 1000 -> -5
         distance <= 2000 -> -10
         else -> -10
     }

    override fun getNextTask(): Single<CityTask> {
        return gameControler.getNextTask()
    }

    override fun prepareNewLevel(newLevel: Int): Completable {
        return gameControler.prepareForLevel(newLevel)

    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameControler.haveTaskForCurrentLevel()
    }


    override fun finishGame() {
        if (taskCounter.value == 0 || taskCounter.value == null) {
            super.finishGame()
            return
        }
        gameControler
            .finishGame(taskCounter.value ?: 0, taskCounter.value ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                error.postValue(GameError.NONE)
                gameInfo.recordId = it?.id
                super.finishGame()
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
            })
    }

}