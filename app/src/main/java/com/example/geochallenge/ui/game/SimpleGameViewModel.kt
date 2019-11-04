package com.example.geochallenge.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.utils.CalculateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SimpleGameViewModel(val isOneAttemp : Boolean = true)  : ViewModel() {

    companion object{
        const val MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL = 8
    }

    private val taskService : TaskService = AppDelegate.taskStorage

    val isDefaultMapState : MutableLiveData<Boolean> = MutableLiveData()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask : MutableLiveData<CityTask> = MutableLiveData()
    val distance : MutableLiveData<Int> = MutableLiveData()
    val taskCounter : MutableLiveData<Int> = MutableLiveData()
    val currentLevel : MutableLiveData<Int> = MutableLiveData()

    lateinit var iteratorTasksForCurrentLevel: Iterator<CityTask>

    init{
        currentTask.observeForever{
            val result = taskCounter.value?.plus(1) ?: 0
            taskCounter.postValue(result)
        }
    }

    open fun newGame(){
        newLevel()
    }
    fun newLevel(){
        val level = currentLevel.value ?:0
        val newLevel = if(level == 0) 1 else level + 1
        taskService.getRandomCityTasksByLevel(newLevel,
            MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                iteratorTasksForCurrentLevel = it.iterator()
                currentLevel.postValue(newLevel)
                nextTask()
            },{
                Log.e("SimpleGameViewModel",it.message)
            })
    }

    open fun nextTask(){

        isDefaultMapState.postValue(true)
        clickedPosition.postValue(null)
        if(!iteratorTasksForCurrentLevel.hasNext()){
            newLevel()
            return
            TODO("а если уровни закончились??")
        }

        val task = iteratorTasksForCurrentLevel.next()
        currentTask.postValue(task)
        isTaskCompleted.postValue(false)
    }

    open fun finishGame(){
        isGameFinished.postValue(true)
    }

    open fun clickedPosition( latitude: Double, longitude: Double){
        isDefaultMapState.postValue(false)
        val currentTaskLat = currentTask.value?.latitude ?: return
        val currentTaskLon =  currentTask.value?.longitude ?: return
        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(currentTaskLat, currentTaskLon)).toInt() / 1000 // m to km
        distance.postValue( d)
        clickedPosition.postValue(Pair(latitude, longitude))

        if(isOneAttemp) isTaskCompleted.postValue(true)
    }

    fun cameraMoved(){
        isDefaultMapState.postValue(false)
    }

}