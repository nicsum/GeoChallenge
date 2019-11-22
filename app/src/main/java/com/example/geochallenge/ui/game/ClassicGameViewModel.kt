package com.example.geochallenge.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.CityTask
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

open class ClassicGameViewModel : SimpleGameViewModel() {

    companion object{

        const val SECONDS_FOR_TASK = 13L
        const val SECONDS_FOR_BONUS = 10L
        const val MAX_POINTS_FOR_LEVEL =
            MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL * (SECONDS_FOR_BONUS.toInt() * 20)
    }

    val secondsPassed = MutableLiveData<Long>()
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
            .intervalRange(1, count + 1, 1, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::finishTask)
            .subscribe {
                secondsPassed.postValue(it)
            }
    }

    override fun levelFinished() {
        super.levelFinished()
        if (neededPointsForNextLevel(currentLevel.value ?: 0) >=
            pointsForCurrentLevel.value ?: 0
        ) {
            finishGame()
        } else {
            nextLevel()
        }
    }

    private fun getTimeBonus(seconds: Long): Long {
        if (seconds > SECONDS_FOR_BONUS) return 0
        return (SECONDS_FOR_BONUS - seconds) * 20
    }

    private fun neededPointsForNextLevel(currentLevel: Int): Int {
        return MAX_POINTS_FOR_LEVEL * when (currentLevel) {
            1 -> 0.5
            2 -> 0.55
            3 -> 0.6
            4 -> 0.65
            5 -> 0.7
            else -> 0.75
        }.toInt()
    }

    private fun calculatePoints(seconds: Long, distance: Int): Int {
        if (distance >= 800) {
            return 0
        }
        return 800 - distance + getTimeBonus(seconds).toInt()
    }

}