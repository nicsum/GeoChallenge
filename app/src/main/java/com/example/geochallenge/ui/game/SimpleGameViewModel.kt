package com.example.geochallenge.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.game.Task
import com.example.geochallenge.game.TaskGenerator
import com.example.geochallenge.game.TaskService
import com.example.geochallenge.utils.CalculateUtils

open class SimpleGameViewModel(val isOneAttemp : Boolean = true)  : ViewModel() {

    private val taskService : TaskService = TaskGenerator()

    val isDefaultMapState : MutableLiveData<Boolean> = MutableLiveData()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask : MutableLiveData<Task> = MutableLiveData()
    val distance : MutableLiveData<Int> = MutableLiveData()
    val taskCounter : MutableLiveData<Int> = MutableLiveData()

    init{
        currentTask.observeForever{
            val result = taskCounter.value?.plus(1) ?: 0
            taskCounter.postValue(result)
        }
    }

    open fun newGame(){
        isDefaultMapState.postValue(true)
        isTaskCompleted.postValue(false)

        val task = taskService.nextTask()
        if(task == null) finishGame()
        else currentTask.postValue(task)
    }


    open fun nextTask(){
        isDefaultMapState.postValue(true)
        isTaskCompleted.postValue(false)
        clickedPosition.postValue(null)
        val task = taskService.nextTask()
        if(task == null) finishGame()
        else {currentTask.postValue(task)}
    }

    open fun finishGame(){
        isGameFinished.postValue(true)

    }

    open fun clickedPosition( latitude: Double, longitude: Double){
        isDefaultMapState.postValue(false)

        val task = currentTask.value ?: return

        val d = CalculateUtils.calculateDistance(
            Pair(latitude, longitude),
            Pair(task.latitude, task.longitude)).toInt() / 1000 // m to km

        distance.postValue( d)
        clickedPosition.postValue(Pair(latitude, longitude))

        if(isOneAttemp) isTaskCompleted.postValue(true)

    }

    fun cameraMoved(){
        isDefaultMapState.postValue(false)
    }

}