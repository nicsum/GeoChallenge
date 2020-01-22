package com.example.geochallenge.ui.game.time

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.TaskAnswer
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.GameError
import com.google.android.gms.maps.model.LatLng
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
        const val DEFAULT_COUNT_TIMER: Long = 30
    }

//    val stillHaveTime = MutableLiveData<Long>()
//    val secondsPassed = MutableLiveData<Long>().also { it.value = 0L }

    val timer =
        MutableLiveData<Pair<Long, Long>>().also { it.value = Pair(0L, DEFAULT_COUNT_TIMER) }
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
                 .delay(1, TimeUnit.SECONDS)
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
                timer.postValue(Pair(0, count))
            }
            .subscribe{
                val timeLeft = timer.value!!.first + 1
                timer.postValue(Pair(timeLeft, count))
            }

    }

    private fun getTimeBonus(distance: Double) = when {

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
        return gameControler
            .getNextTask()
            .doOnSuccess { startTimer(timeLeft) }

    }

    override fun prepareNewLevel(newLevel: Int): Completable {
        return gameControler.prepareForLevel(newLevel)

    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameControler.haveTaskForCurrentLevel()
    }


    override fun finishGame() {
        if (taskCounter.value == 0 || taskCounter.value == null) {
            gameResult.postValue(Pair(0, false))
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
                val score = if (it.updated) it.score else taskCounter.value ?: 0
                gameResult.postValue(Pair(score, it.updated))
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
            })
    }

}