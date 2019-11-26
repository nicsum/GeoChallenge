package com.example.geochallenge.game.multiplayer

import android.util.Log
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.MultiplayGameState
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

class FirebaseMultiplayerControler(
    val taskService: TaskService,
    val api: MultiplayerApi,
    val countTasksForLevel: Int
) : MultiplayerControler {

    val database = FirebaseFirestore.getInstance()

    var gameStateChangeListener: GameStateChangeListener? = null

    var currentGameState: MultiplayGameState? = null

    var currentLevel = 1

    //TODO удалить
    var countTasks = 0

    override fun startGame(listener: GameStateChangeListener) {
        gameStateChangeListener = listener

        getTasksForGame()
            .map { it.map { task -> task.id } }
            .flatMap { taskIds ->
                val userId = getUserId() ?: return@flatMap Single.error<Response>(
                    FirebaseAuthException("user is not logged in", "user is not logged in")
                ) //TODO хз, может свой ексепшн написать когда пользователь не авторизован
                api.startGame(taskIds.filterNotNull(), userId)
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

    override fun postAnswer(coordinates: Pair<Double, Double>): Completable {
        val userId = getUserId() ?: return Completable.error(
            FirebaseAuthException(
                "user is not logged in",
                "user is not logged in"
            )
        )
        val gameId = currentGameState?.id ?: return Completable.error(Exception("вы не в игре"))
        return api.postAnswer(coordinates, userId, gameId)
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
                        if (gameState.currentTask >= gameState.tasks.size - 1) {
                            badGetTaskForNextLevel(gameState.tasks)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe()
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
            oldGameState.status != newGameState.status && newGameState.status == 1 -> {
                countTasks = 1
                listener.onStartGame(newGameState.tasks[0])
            }


            oldGameState.currentTask != newGameState.currentTask -> {
                countTasks++
                listener.onNextTask(newGameState.getIdCurrentTask())
            }


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
            countTasksForLevel
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
            return taskService.getRandomCityTasksByLevel(
                currentLevel + 1,
                countTasksForLevel
            )
                .map { (oldTasks + it.map { tc -> tc.id }) }
                .flatMapCompletable { tasks ->
                    database.collection("gameState").document(gameId)
                        .update("tasks", tasks.toString())
                    Completable.complete()
                }

        }

        return Completable.error(Exception("gamestate = null"))

    }

}

