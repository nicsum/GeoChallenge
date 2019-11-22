package com.example.geochallenge.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.SinglePlayerLevelProvider
import com.example.geochallenge.utils.CalculateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SimpleGameViewModel(
    val taskService: TaskService = AppDelegate.taskStorage,
    open var levelProvider: LevelProvider =
        SinglePlayerLevelProvider(taskService, MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL)
) : ViewModel() {

    companion object{
        const val MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL = 5
    }

    val isDefaultMapState : MutableLiveData<Boolean> = MutableLiveData()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask : MutableLiveData<CityTask> = MutableLiveData()
    val distance : MutableLiveData<Int> = MutableLiveData()
    val taskCounter : MutableLiveData<Int> = MutableLiveData()
    val currentLevel : MutableLiveData<Int> = MutableLiveData()
    val isLevelFinished: MutableLiveData<Boolean> = MutableLiveData()

//    lateinit var iteratorTasksForCurrentLevel: Iterator<CityTask>


    open fun newGame(){
        nextLevel()
    }

    protected open fun nextLevel() {
        val level = currentLevel.value ?:0
        val newLevel = if(level == 0) 1 else level + 1
        levelProvider.prepareForLevel(newLevel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //                iteratorTasksForCurrentLevel = it.iterator()
                currentLevel.postValue(newLevel)
                isLevelFinished.postValue(false)
                nextTask()
            },{
                Log.e("SimpleGameViewModel",it.message)
            })


//        taskService.getRandomCityTasksByLevel(newLevel,
//            MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                iteratorTasksForCurrentLevel = it.iterator()
//                currentLevel.postValue(newLevel)
//                isLevelFinished.postValue(false)
//                nextTask()
//            },{
//                Log.e("SimpleGameViewModel",it.message)
//            })
    }

    open fun nextTask(){

        isDefaultMapState.postValue(true)
        clickedPosition.postValue(null)
        if (!levelProvider.haveTaskForCurrentLevel()) {
            levelFinished()
            return
        }

        levelProvider.getNextTask()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                currentTask.postValue(it)
                isTaskCompleted.postValue(false)
                taskCounter.postValue(taskCounter.value?.plus(1) ?: 0)
            }, {
                Log.e("SimpleGameViewModel", it.message)
            })

    }

    open fun finishGame(){
        isGameFinished.postValue(true)
    }

    open fun clickedPosition(latitude: Double, longitude: Double) {
        val currentTaskLat = currentTask.value?.latitude ?: return
        val currentTaskLon =  currentTask.value?.longitude ?: return
        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(currentTaskLat, currentTaskLon)).toInt() / 1000 // m to km

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

    fun cameraMoved(){
        isDefaultMapState.postValue(false)
    }

}