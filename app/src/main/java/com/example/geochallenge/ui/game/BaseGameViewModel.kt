package com.example.geochallenge.ui.game

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.TaskAnswer
import com.example.geochallenge.utils.CalculateUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException

abstract class BaseGameViewModel : ViewModel() {

    val isDefaultMapState: MutableLiveData<Boolean> = MutableLiveData()
    val taskAnswer = MutableLiveData<TaskAnswer>()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    //    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask: MutableLiveData<CityTask> = MutableLiveData()
    val distance: MutableLiveData<Int> = MutableLiveData()
    val taskCounter: MutableLiveData<Int> = MutableLiveData()
    val currentLevel: MutableLiveData<Int> = MutableLiveData()
    val isLevelFinished: MutableLiveData<Boolean> = MutableLiveData()
    val error = MutableLiveData<GameError>().also { it.value = GameError.NONE }
    val isErrorVisible = MutableLiveData<Boolean>()
    val isLoadingVisible = MutableLiveData<Boolean>()
    val isGameInfoVisible = MutableLiveData<Boolean>()
    val gameResult = MutableLiveData<Pair<Int, Boolean>>()

    var cityTask: CityTask? = null

    open fun newGame() {
        nextLevel()
    }

    open fun updateTasks() {
        prepareNewLevel(currentLevel.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .subscribe({
                error.postValue(GameError.NONE)
                isLevelFinished.postValue(false)
                nextTask()
            }, {
                resolveError(it)
                Log.e("SimpleGameViewModel", it.message)
            })
    }
    protected open fun nextLevel() {
        val level = currentLevel.value ?: 0
        val newLevel = if (level == 0) 1 else level + 1
        prepareNewLevel(newLevel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { currentLevel.postValue(newLevel) }
            .doOnSubscribe {
                showLoading()
            }
            .subscribe({
                error.postValue(GameError.NONE)
                nextTask()
            }, {
                resolveError(it)
                Log.e("SimpleGameViewModel", it.message)
            })
    }

    protected open fun resolveError(e: Throwable) {

        when (e) {
            is HttpException -> error.postValue(GameError.SERVER_ERROR)
            is IOException -> error.postValue(GameError.CONNECTION_ERROR)
            else -> error.postValue(GameError.ANY)
        }
        showError()
        Log.e("BaseGameViewModel", e.message)
    }

    open fun nextTask() {

        Log.i("BaseGameViewModel", Thread.currentThread().name)

        if (!haveTaskForCurrentLevel()) {
            levelFinished()
            return
        }
        getNextTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoading() }
            .doOnSuccess {
                error.postValue(GameError.NONE)
            }
            .subscribe({
                onStartTask(it)
                showGameInfo()
            }, {
                resolveError(it)
                Log.e("SimpleGameViewModel", it.message)
            })
    }

    open fun onStartTask(task: CityTask) {
        setTask(task)
        isDefaultMapState.postValue(true)
        clickedPosition.postValue(null)
        isTaskCompleted.postValue(false)
        taskAnswer.postValue(null)
        taskCounter.postValue(taskCounter.value?.plus(1) ?: 1)
    }

    open fun clickedPosition(latitude: Double, longitude: Double) {
        clickedPosition.postValue(Pair(latitude, longitude))
        val currentTaskLat = currentTask.value?.latitude ?: return
        val currentTaskLon = currentTask.value?.longitude ?: return
        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(currentTaskLat, currentTaskLon)
        ).toInt() / 1000 // m to km

        clickedPosition(latitude, longitude, d)
    }

    protected open fun clickedPosition(
        latitude: Double,
        longitude: Double, distance: Int
    ) {
        this.distance.postValue(distance)
//        val answer = TaskAnswer(LatLng(latitude, longitude), currentTask.value!!)
//        finishTask(answer)
    }

    protected open fun finishTask() {
        isTaskCompleted.postValue(true)
    }

    open fun finishGame() {}

    protected open fun levelFinished() {
        isLevelFinished.postValue(true)
        taskCounter.postValue(0)
    }


    @MainThread
    fun cameraMoved() {
        isDefaultMapState.value = false
    }

    private fun showGameInfo() {
        isLoadingVisible.postValue(false)
        isErrorVisible.postValue(false)
        isGameInfoVisible.postValue(true)
    }

    private fun showError() {
        isLoadingVisible.postValue(false)
        isErrorVisible.postValue(true)
        isGameInfoVisible.postValue(false)
    }

    private fun showLoading() {
        isLoadingVisible.postValue(true)
        isErrorVisible.postValue(false)
        isGameInfoVisible.postValue(false)
    }

    private fun setTask(task: CityTask) {
        this.cityTask = task
        this.currentTask.postValue(task)
    }

    abstract fun getNextTask(): Single<CityTask>
    abstract fun prepareNewLevel(newLevel: Int): Completable

    abstract fun haveTaskForCurrentLevel(): Boolean

}