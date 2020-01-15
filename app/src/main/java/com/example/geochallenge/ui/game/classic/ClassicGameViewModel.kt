package com.example.geochallenge.ui.game.classic

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
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

open class ClassicGameViewModel(
    val gameControler: GameControler,
    private val gameMap: GameMap,
    val gameInfo: GameInfo
) :
    BaseGameViewModel() {

    companion object{
        const val SECONDS_FOR_TASK = 13L
        const val SECONDS_FOR_BONUS = 10L
    }

    val maxPointsForLevel = gameInfo.countTaskForLevel * (SECONDS_FOR_BONUS.toInt() * 20)

    val secondsPassed = MutableLiveData<Long>().also { it.value = 0L }
    var points = MutableLiveData<Int>()
    var pointsForCurrentLevel = MutableLiveData<Int>()


    var timerDisposable: Disposable? = null

    override fun onStartTask(task: CityTask) {
        super.onStartTask(task)
        startTimerFromCount(SECONDS_FOR_TASK)
        pointsForCurrentLevel.postValue(0)
    }

    override fun clickedPosition(latitude: Double, longitude: Double, distance: Int) {
        super.clickedPosition(latitude, longitude, distance)

        //calculate points
        val seconds = secondsPassed.value ?: return
        val pointsForCurrentTask = calculatePoints(seconds, distance)
        points.postValue((points.value ?: 0) + pointsForCurrentTask)
        pointsForCurrentLevel.postValue((pointsForCurrentLevel.value ?: 0) + pointsForCurrentTask)

        Log.i(
            "ClassicGameViewModelTag",
            "distance = $distance, seconds = $seconds , points = $pointsForCurrentTask"
        )
    }

    override fun finishTask() {
        super.finishTask()
        timerDisposable?.dispose()
    }

    fun startTimerFromCount(count: Long) {
        timerDisposable?.dispose()

        timerDisposable = Observable
            .intervalRange(0, count + 1, 1, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::finishTask)
            .subscribe {
                secondsPassed.postValue(it)
            }
    }

    override fun levelFinished() {
        super.levelFinished()
        if (neededPointsForNextLevel() >=
            pointsForCurrentLevel.value ?: 0
        ) {
            finishGame()
        } else {
            nextLevel()
        }
    }

    override fun finishGame() {
        if (points.value == 0 || points.value == null) {
            gameResult.postValue(Pair(0, false))
            super.finishGame()
            return
        }
        gameControler
            .finishGame(points.value ?: 0, taskCounter.value ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                error.postValue(GameError.NONE)
                gameInfo.recordId = it?.id
                val score = if (it.updated) it.score else points.value ?: 0
                gameResult.postValue(Pair(score, it.updated))
                super.finishGame()
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
            })
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

    private fun getTimeBonus(seconds: Long): Long {
        if (seconds > SECONDS_FOR_BONUS) return 0
        return (SECONDS_FOR_BONUS - seconds) * 20
    }

    private fun neededPointsForNextLevel(): Int {
        return (maxPointsForLevel * 0.5).toInt()
    }

    private fun calculatePoints(seconds: Long, distance: Int): Int {
        val limitDistance = gameMap.distance
            ?.toInt() ?: 800
        if (distance >= limitDistance) {
            return 0
        }
        return limitDistance - distance + getTimeBonus(seconds).toInt()
    }

}