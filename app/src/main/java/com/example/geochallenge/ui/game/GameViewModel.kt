package com.example.geochallenge.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.game.Task
import com.example.geochallenge.game.TaskGenerator
import com.example.geochallenge.game.TaskService
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import io.reactivex.disposables.Disposable
import kotlin.math.max
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GameViewModel: ViewModel() {

    companion object {
        const val COUNT_TIMER : Long = 10
    }

    var taskService : TaskService = TaskGenerator()

    var currentTask : MutableLiveData<Task> = MutableLiveData()
    var currentTaskPoints : MutableLiveData<Int> = MutableLiveData()
    var clickedPositions : MutableLiveData<Pair<LatLng?, String?>> = MutableLiveData() // String - distance of clicked position to true position
    var distance : MutableLiveData<Int> = MutableLiveData()
    var taskCompeted: MutableLiveData<Boolean> = MutableLiveData()
    var timerText : MutableLiveData<String> = MutableLiveData()

    var gamePoints : MutableLiveData<Int> = MutableLiveData()
    var gameFinished: MutableLiveData<Boolean> = MutableLiveData()



    lateinit var timerDisposable: Disposable

    fun onClickPosition(position: LatLng){

        val newDistance = calculateDistance(position)
        distance.postValue(newDistance)

        currentTaskPoints.postValue(max(currentTaskPoints.value ?: 0, calculatePoints(newDistance)))
        clickedPositions.postValue(Pair(position, "$newDistance км."))

    }

    fun newTask(){
        var task = taskService.nextTask()
        if(task == null){
            taskService = TaskGenerator()
            task = taskService.nextTask()
        }

        currentTask.postValue(task)
        currentTaskPoints.postValue(0)
        distance.postValue(null)
        taskCompeted.postValue(false)
        startTimer()
    }

    //?
    fun taskComplected(){
        taskCompeted.postValue(true)
        gamePoints.postValue((gamePoints.value ?: 0) + (currentTaskPoints.value ?: 0))

        if(!timerDisposable.isDisposed) timerDisposable.dispose()
    }


    fun newGame(){
        gameFinished.postValue(false)
        currentTaskPoints.postValue(0)
        clickedPositions.postValue(null)
        distance.postValue(null)
        taskCompeted.postValue(false)
        gamePoints.postValue(0)
        newTask()
    }

    fun startTimer(){
        timerText.postValue("")

        timerDisposable = Observable
            .intervalRange(0, COUNT_TIMER+1,0,1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::taskComplected)
            .subscribe{timerText.postValue( "$it сек.")}

    }

    fun finishGame(){
        taskComplected()
        gameFinished.postValue(true)
    }

    private fun calculateDistance(position: LatLng):Int?{
        val task = currentTask.value
        if( task!= null){
            return SphericalUtil.computeDistanceBetween(LatLng(task.latitude, task.longitude), position).toInt() / 1000
        }
        return null
    }

    private fun calculatePoints(distance: Int?): Int{

        return  if(distance != null) max(0, 1000 - distance) else 0
    }


}