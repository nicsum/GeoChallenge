package com.example.geochallenge.game.multiplayer

import android.util.Log
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.MultiplayGameState
import com.example.geochallenge.ui.game.SimpleGameViewModel
import com.example.geochallenge.utils.getGameId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Response

class MultiplayerControler(val taskService: TaskService) {

    interface GameStateChangeListener {
        fun onNextTask(taskId: Int)
        fun onStartGame(taskId: Int)
        fun onPlayerMakeAnswer(playersAnswer: Map<String, Pair<Double, Double>?>)
        fun onFinishGame()
        fun onFailStartGame(throwable: Throwable)
        fun onFailContinueGame(throwable: Throwable)
    }


    val firebaseApi: MultiplayerApi =
        FirebaseMultiplayerApi()

    val database = FirebaseFirestore.getInstance()

    var currentGameState: MultiplayGameState? = null

    var gameStateChangeListener: GameStateChangeListener? = null

    var currentLevel = 1

    fun startGame(listener: GameStateChangeListener) {
        gameStateChangeListener = listener

        getTasksForGame()
            .map { it.map { task -> task.id } }
            .flatMap { taskIds ->
                val userId = getUserId() ?: return@flatMap Single.error<Response>(
                    FirebaseAuthException("user is not logged in", "user is not logged in")
                ) //TODO хз, может свой ексепшн написать когда пользователь не авторизован
                firebaseApi.startGame(taskIds.filterNotNull(), userId)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val gameId = getGameId(response)
                    ?: throw Exception("не удалось создать игру или подключиться к ней") //TODO
                watchGameState(gameId, listener)

            }, {
                listener.onFailStartGame(it)
            })

    }

    fun postAnswer(coordinates: Pair<Double, Double>): Completable {
        val userId = getUserId() ?: return Completable.error(
            FirebaseAuthException(
                "user is not logged in",
                "user is not logged in"
            )
        )
        val gameId = currentGameState?.id ?: return Completable.error(Exception("вы не в игре"))
        return firebaseApi.postAnswer(coordinates, userId, gameId)
    }

    private fun watchGameState(gameId: String, listener: GameStateChangeListener) {
        database.collection("gameState").document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    listener.onFailContinueGame(e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    var gameState: MultiplayGameState? = null
                    try {
                        gameState = getGameState(snapshot)
                        //TODO bad!
                        if (gameState.tasks.size == gameState.currentTask) {
                            badGetTaskForNextLevel(gameState.tasks)
                        }
                    } catch (e: Throwable) {
                        Log.d("tag", e.message)
                    }

                    if (currentGameState != null && gameState != null) {
                        val oldGameState = currentGameState
                        currentGameState = gameState
                        // поведение игры определяем по старому и новому состоянию
                        whatDo(oldGameState!!, gameState, listener)
                        return@addSnapshotListener
                    }
                    //начало игры
                    if (currentGameState == null) {
                        currentGameState = gameState
                        currentGameState?.let {
                            if (it.status == 1) listener.onStartGame(it.tasks[0])
                            return@addSnapshotListener
                        }
                    }
                }
            }
    }

    private fun whatDo(
        oldGameState: MultiplayGameState, newGameState: MultiplayGameState,
        listener: GameStateChangeListener
    ) {
        when {
            oldGameState.status != newGameState.status && newGameState.status == 1 ->
                listener.onStartGame(newGameState.tasks[0])

            oldGameState.currentTask != newGameState.currentTask ->
                listener.onNextTask(newGameState.getIdCurrentTask())

            (oldGameState.status == 1) && (oldGameState.players != newGameState.players) ->
                listener.onPlayerMakeAnswer(newGameState.players
                    .filterKeys { it != getUserId() }
                    .mapValues { player -> player.value?.let { Pair(it[0], it[1]) } }
                )

            newGameState.status == 2 ->
                listener.onFinishGame()
        }
    }

    private fun getTasksForGame(): Single<List<CityTask>> {
        return taskService.getRandomCityTasksByLevel(
            1,
            SimpleGameViewModel.MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL
        )
    }

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    private fun getGameState(snapshot: DocumentSnapshot): MultiplayGameState {
        val json = snapshot.data.toString()
        val gameState = Gson().fromJson(json, MultiplayGameState::class.java)
        gameState.id = snapshot.id
        return gameState
    }


    private fun badGetTaskForNextLevel(oldTasks: List<Int>): Completable {
        currentGameState?.id?.let { gameId ->
            taskService.getRandomCityTasksByLevel(
                currentLevel + 1,
                SimpleGameViewModel.MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL
            )
                .map { oldTasks + it }
                .flatMapCompletable {
                    database.collection("gameState").document(gameId)
                        .update("tasks", it.toString())
                    Completable.complete()
                }

        }

        return Completable.error(Exception("gamestate = null"))

    }

}

