package com.example.geochallenge.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.game.Task
import com.example.geochallenge.game.TaskRepository
import com.example.geochallenge.game.TaskService
import com.example.geochallenge.utils.CalculateUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SimpleGameViewModel(val isOneAttemp : Boolean = true)  : ViewModel() {

    private val taskService : TaskService = TaskRepository()

    val isDefaultMapState : MutableLiveData<Boolean> = MutableLiveData()
    val isTaskCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isGameFinished: MutableLiveData<Boolean> = MutableLiveData()
    val clickedPosition: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val currentTask : MutableLiveData<Task> = MutableLiveData()
    val distance : MutableLiveData<Int> = MutableLiveData()
    val taskCounter : MutableLiveData<Int> = MutableLiveData()
    val taskLevel : MutableLiveData<Int> = MutableLiveData()

    init{
        currentTask.observeForever{
            val result = taskCounter.value?.plus(1) ?: 0
            taskCounter.postValue(result)
        }
    }


    open fun newGame(){
            taskService.nextTask()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isDefaultMapState.postValue(true)
            }
            .subscribe({
                currentTask.postValue(it)
                isTaskCompleted.postValue(false)
                taskLevel.postValue(it?.level)
            },{})

    }


    open fun nextTask(){
        taskService.nextTask()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                isDefaultMapState.postValue(true)
                clickedPosition.postValue(null)
            }
            .subscribe({
                currentTask.postValue(it)
                isTaskCompleted.postValue(false)
                taskLevel.postValue(it?.level)
            },{})

    }

    open fun finishGame(){
        isGameFinished.postValue(true)

    }

    open fun clickedPosition( latitude: Double, longitude: Double){
        isDefaultMapState.postValue(false)
        val currentTaskLat = currentTask.value?.Lat ?: return
        val currentTaskLon =  currentTask.value?.lng ?: return
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