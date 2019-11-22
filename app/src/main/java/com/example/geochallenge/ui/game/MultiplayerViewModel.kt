package com.example.geochallenge.ui.game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.MultiplayerLevelProvider
import com.example.geochallenge.game.multiplayer.MultiplayerControler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MultiplayerViewModel(
    override var levelProvider: LevelProvider = MultiplayerLevelProvider()
) : ClassicGameViewModel(), MultiplayerControler.GameStateChangeListener {

    var multiplayerControler = MultiplayerControler(taskService)
//    var nextTask: CityTask? = null

    var playersAnswer = MutableLiveData<Map<String, Pair<Double, Double>?>>()

    override fun newGame() {
        super.newGame()
        multiplayerControler.startGame(this)

    }

    override fun clickedPosition(latitude: Double, longitude: Double) {

        multiplayerControler
            .postAnswer(Pair(latitude, longitude))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                super.clickedPosition(latitude, longitude)
            }, {

            })
    }

//    override fun nextTask() {
//        nextTask?.let{
//
//        }
//        nextTask?.let {
//            currentTask.postValue(nextTask)
//            nextTask = null
//            isTaskCompleted.postValue(false)
//            isDefaultMapState.postValue(true)
//            clickedPosition.postValue(null)
//        }
//
//    }

    override fun onNextTask(taskId: Int) {
        taskService.getCityTaskById(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (levelProvider as MultiplayerLevelProvider).postTask(it)

//                nextTask = it
//                isTaskCompleted.postValue(true)
            }, {
                Log.e("MultiplayerViewModel", it.message)
            })
    }

    override fun onStartGame(taskId: Int) {
        taskService.getCityTaskById(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                currentTask.postValue(it)
                isTaskCompleted.postValue(false)
                isDefaultMapState.postValue(true)
                clickedPosition.postValue(null)
            }, {
                Log.e("MultiplayerViewModel", it.message)
            })

    }

    override fun onPlayerMakeAnswer(playersAnswer: Map<String, Pair<Double, Double>?>) {
        this.playersAnswer.postValue(playersAnswer)
    }

    override fun onFinishGame() {
        finishGame()
    }

    override fun onFailStartGame(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailContinueGame(throwable: Throwable) {
        Log.d("MultiplayerViewModel", throwable.message)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}