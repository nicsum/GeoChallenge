package com.example.geochallenge.ui.game.time

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.TaskAnswer
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameError
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class TimeLimitGameViewModel(
    val gameControler: GameControler,
    val gameMap: GameMap,
    val gameInfo: GameInfo
) : BaseGameViewModel() {

    companion object {
        const val DEFAULT_COUNT_TIMER: Long = 30
    }

//    val stillHaveTime = MutableLiveData<Long>()
//    val secondsPassed = MutableLiveData<Long>().also { it.value = 0L }

    val timer =
        MutableLiveData<Pair<Long, Long>>(0L to DEFAULT_COUNT_TIMER)

    var timeLeft = DEFAULT_COUNT_TIMER

    var timerDisposable: Disposable? = null


    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
         super.clickedPosition(latitude, longitude, distance)
         Log.i("BaseGameViewModel", Thread.currentThread().name)
         //calculate time
         val timeBonus = getTimeBonus(distance)
         val resultTime = timer.value!!.second - timer.value!!.first + timeBonus

        val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))

         cityTask?.name?.let {
             gameControler.postGameStats(it, distance)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .onErrorComplete()
                 .subscribe()
         }

         taskAnswer.postValue(answer)

         if (resultTime <= 0) {
             finishGame()
             timeLeft = 0
         } else {
             timeLeft = resultTime
             Observable.just(1)
                 .subscribeOn(AndroidSchedulers.mainThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .delay(2, TimeUnit.SECONDS)
                 .subscribe {
                     nextTask()
                 }
         }
     }


    override fun finishTask() {
        timerDisposable?.dispose()
        super.finishTask()
    }

     override fun levelFinished() {
         super.levelFinished()
         nextLevel()
     }

    fun startTimer(count: Long) {
        timerDisposable?.dispose()
        timerDisposable = Observable
            .intervalRange(0, count + 1, 1, 1, TimeUnit.SECONDS)
            .doOnComplete(this::finishGame)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                timer.postValue(0L to count)
            }
            .subscribe{
                val timeLeft = timer.value!!.first + 1
                timer.postValue(timeLeft to count)
            }

    }

    private fun getTimeBonus(distance: Double): Int {
        val percent = distance / getLimitDistance()

        return when {
            percent <= 0.1 -> 9
            percent <= 0.2 -> 8
            percent <= 0.4 -> 7
            percent <= 0.6 -> 6
            percent <= 0.8 -> 5
            percent <= 1 -> 0
            percent <= 1.2 -> -2
            percent <= 1.4 -> -3
            percent <= 1.6 -> -4
            percent <= 1.8 -> -5
            percent <= 2 -> -10
            else -> -10
        }
    }

    override fun getNextTask(): Single<CityTask> {
        return gameControler
            .getNextTask()
            .doOnSuccess { startTimer(timeLeft) }
    }

    override fun prepareNewLevel(newLevel: Int): Single<Int> {
        return gameControler.prepareForLevel(newLevel)
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameControler.haveTaskForCurrentLevel()
    }

    override fun finishGame() {
        timerDisposable?.dispose()
        if (taskCounterLevel.value == 0 || taskCounterLevel.value == null) {
            gameResult.postValue(Pair(0, false))
            super.finishGame()
            return
        }
        gameControler
            .finishGame(taskCounterGame.value?.minus(1) ?: 0, taskCounterGame.value ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                super.finishGame()
                error.postValue(GameError.NONE)
                gameInfo.recordId = it?.id
                val score = if (it.updated) it.score else taskCounterGame.value ?: 0
                gameResult.postValue(Pair(score, it.updated))
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
                Log.i("tag", it.message)
            })
    }

    private fun getLimitDistance() = gameMap.distance ?: DEFAULT_DISTANCE
}