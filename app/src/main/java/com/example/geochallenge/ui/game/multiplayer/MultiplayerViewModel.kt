package com.example.geochallenge.ui.game.multiplayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.controlers.GameControler
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerDispatcher
import com.example.geochallenge.game.multiplayer.GameStateChangeListener
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MultiplayerViewModel(
    gameControler: GameControler,
    val multiplayerControler: FirebaseMultiplayerDispatcher,
    val geochallengeService: GeochallengeService,
    gameMap: GameMap,
    gameInfo: GameInfo
) : ClassicGameViewModel(gameControler, gameMap, gameInfo), GameStateChangeListener {


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
        TODO()
//        geochallengeService.getCityTaskById(taskId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                (gameControler as MultiplayerGameControler).postTask(it)
//                nextTask()
////                nextTask = it
////                isTaskCompleted.postValue(true)
//            }, {
//                Log.e("MultiplayerViewModel", it.message)
//            })
    }

    override fun onStartGame(taskId: Int) {
        TODO()
//        geochallengeService.getCityTaskById(taskId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                waitingPlayers.postValue(false)
//                (gameControler as MultiplayerGameControler).postTask(it)
//                nextTask()
//            }, {
//                Log.e("MultiplayerViewModel", it.message)
//            })

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