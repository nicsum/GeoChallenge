package com.example.geochallenge.ui.game.multiplayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.levels.LevelProvider
import com.example.geochallenge.game.levels.MultiplayerLevelProvider
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerControler
import com.example.geochallenge.game.multiplayer.GameStateChangeListener
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MultiplayerViewModel(
    levelProvider: LevelProvider,
    val multiplayerControler: FirebaseMultiplayerControler,
    val taskService: TaskService,
    countTasksForLevel: Int
) : ClassicGameViewModel(levelProvider, countTasksForLevel), GameStateChangeListener {


    var playersAnswer = MutableLiveData<Map<String, Pair<Double, Double>?>>()
    var waitingPlayers = MutableLiveData<Boolean>()

    override fun newGame() {
        super.newGame()
        waitingPlayers.postValue(true)
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


    override fun onNextTask(taskId: Int) {
        taskService.getCityTaskById(taskId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                (levelProvider as MultiplayerLevelProvider).postTask(it)
                nextTask()
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
                waitingPlayers.postValue(false)
                (levelProvider as MultiplayerLevelProvider).postTask(it)
                nextTask()
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