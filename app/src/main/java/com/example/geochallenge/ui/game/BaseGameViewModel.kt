package com.example.geochallenge.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.utils.CalculateUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class BaseGameViewModel : ViewModel() {

    @Inject
    lateinit var viewModel: BaseGameViewModel


    val isDefaultMapState: MutableLiveData<Boolean> = MutableLiveData()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask: MutableLiveData<CityTask> = MutableLiveData()
    val distance: MutableLiveData<Int> = MutableLiveData()
    val taskCounter: MutableLiveData<Int> = MutableLiveData()
    val currentLevel: MutableLiveData<Int> = MutableLiveData()
    val isLevelFinished: MutableLiveData<Boolean> = MutableLiveData()
    open fun newGame() {
        nextLevel()
    }


    protected open fun nextLevel() {
        val level = currentLevel.value ?: 0
        val newLevel = if (level == 0) 1 else level + 1
        prepareNewLevel(newLevel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                iteratorTasksForCurrentLevel = it.iterator()
                currentLevel.postValue(newLevel)
                isLevelFinished.postValue(false)
                nextTask()
            }, {
                Log.e("SimpleGameViewModel", it.message)
            })

    }

    open fun nextTask() {
        isDefaultMapState.setValue(true)
        clickedPosition.setValue(null)
        isTaskCompleted.setValue(false)
        if (!haveTaskForCurrentLevel()) {
            levelFinished()
            return
        }
        getNextTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onStartTask(it)
            }, {
                Log.e("SimpleGameViewModel", it.message)
            })

    }

    open fun onStartTask(task: CityTask) {
        currentTask.postValue(task)
        taskCounter.postValue(taskCounter.value?.plus(1) ?: 0)
    }

    open fun finishGame() {
        isGameFinished.postValue(true)
    }

    open fun clickedPosition(latitude: Double, longitude: Double) {
        val currentTaskLat = currentTask.value?.latitude ?: return
        val currentTaskLon = currentTask.value?.longitude ?: return
        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(currentTaskLat, currentTaskLon)
        ).toInt() / 1000 // m to km

        clickedPosition(latitude, longitude, d)

    }

    protected open fun levelFinished() {
        isLevelFinished.postValue(true)
    }

    protected open fun clickedPosition(latitude: Double, longitude: Double, distance: Int) {
        this.distance.postValue(distance)
        clickedPosition.postValue(Pair(latitude, longitude))
        finishTask()
    }


    protected open fun finishTask() {
        isTaskCompleted.postValue(true)
    }

    fun cameraMoved() {
        isDefaultMapState.postValue(false)
    }


    abstract fun getNextTask(): Single<CityTask>

    abstract fun prepareNewLevel(newLevel: Int): Completable

    abstract fun haveTaskForCurrentLevel(): Boolean


}