package com.example.geochallenge.game.multiplayer

import android.net.Uri
import android.util.Log
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.ui.game.SimpleGameViewModel
import com.example.geochallenge.utils.getGameId
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class MultiplayerControler(val taskService: TaskService) {

    /*
    TODO 1.отправляем запрос в firebase на новую игру (передаём лист тасков), в ответ получаем
        id gameState и свой id
       2. наблюдаем за gameState
       3. ждем когда state = "go task" и отображаем первый таск
       4. отправляем запрос в firebase с ответом состоящий из долготы и широты
       5. ждем когда state = "task finished"
       6. повторяем 2-6 пока gameState != "game finished"
    */

    interface GameStateChangeListener {
        fun onNextTask(taskId: Int)
        fun onStartGame(taskId: Int)
        fun onPlayerMakeAnswer(playersAnswer: Map<String, Pair<Double, Double>?>)
        fun onFinishGame()
        fun onFailStartGame(throwable: Throwable)
        fun onFailContinueGame(throwable: Throwable)
    }

    val baseUrlStartNewGame = "https://europe-west1-geochallenge.cloudfunctions.net/multiplayerGame"
    val baseUrlAddAnswer = "https://europe-west1-geochallenge.cloudfunctions.net/addAnswer"
    val database = FirebaseFirestore.getInstance()

    var currentGameState: GameState? = null

    val client = OkHttpClient()
    var gameStateChangeListener: GameStateChangeListener? = null

    fun startGame(listener: GameStateChangeListener) {
        gameStateChangeListener = listener

        getTasksForGame()
            .map { it.map { task -> task.id } }
            .flatMap { taskIds ->
                Single.defer {
                    try {
                        val url = getUrlStartNewGame(taskIds.toString())
                        val request = Request.Builder().url(url).build()
                        val response = client.newCall(request).execute()
                        Single.just(response)
                    } catch (e: IOException) {
                        Single.error<Response>(e)
                    }
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                val gameId = getGameId(response)
                gameId?.let {
                    watchGameState(it, listener)
                }
            }, {
                listener.onFailStartGame(it)
            })

    }

    fun postAnswer(coordinates: Pair<Double, Double>): Completable {
        val url = getUrlAddAnswer(coordinates)
        return Completable.defer {
            val request = Request.Builder().url(url).build()
            try {
                client.newCall(request).execute()
                Completable.complete()
            } catch (e: IOException) {
                Completable.error(e)
            }
        }
    }

    private fun watchGameState(gameId: String, listener: GameStateChangeListener) {
        database.collection("gameState").document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    listener.onFailContinueGame(e)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    var gameState: GameState? = null
                    try {
                        val json = snapshot.data.toString()
                        gameState = Gson().fromJson(json, GameState::class.java)
//                        gameState = snapshot.toObject(GameState::class.java)
                    } catch (e: Throwable) {
                        Log.d("tag", e.message)
                    }

                    if (currentGameState != null && gameState != null) {
                        val oldGameState = currentGameState
                        currentGameState = gameState
                        whatDo(oldGameState!!, gameState, listener)
                        return@addSnapshotListener
                    }

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
        oldGameState: GameState, newGameState: GameState,
        listener: GameStateChangeListener
    ) {
        when {
            oldGameState.status != newGameState.status && newGameState.status == 1 ->
                listener.onStartGame(newGameState.tasks[0])

            oldGameState.currentTask != newGameState.currentTask ->
                listener.onNextTask(newGameState.getIdCurrentTask())

            (oldGameState.status == 1) && (oldGameState.players != newGameState.players) ->
                listener.onPlayerMakeAnswer(newGameState.players.filterKeys { it != getUserId() })

            newGameState.status == 2 -> listener.onFinishGame()
        }
    }

    private fun getUrlStartNewGame(tasks: String): String {

        return Uri.Builder().encodedPath(baseUrlStartNewGame)
            .appendQueryParameter("userId", getUserId())
            .appendQueryParameter("tasks", tasks)
            .toString()
    }

    private fun getUrlAddAnswer(coordinates: Pair<Double, Double>): String {

        return Uri.Builder().encodedPath(baseUrlAddAnswer)
            .appendQueryParameter("userId", getUserId())
            .appendQueryParameter("gameId", currentGameState?.id)
            .appendQueryParameter("geoPoint", coordinates.toList().toString())
            .toString()
    }

    private fun getTasksForGame(): Single<List<CityTask>> {
        return taskService.getRandomCityTasksByLevel(
            1,
            SimpleGameViewModel.MINIMUM_COUNT_TASKS_FOR_ONE_LEVEL
        )

//            .subscribe({
//                iteratorTasksForCurrentLevel = it.iterator()
//
//            },{
//                Log.e("SimpleGameViewModel",it.message)
//            })
//       return listOf(1,2,3,4,5,6,7)
    }

    private fun getUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

}

//private fun DocumentSnapshot.toGameState():GameState{
//    return GameState(
//        this["id"].toString(),
//        this.get("currentTask", Int::class.java)!!,
//        this.get("status",  Int::class.java)!!,
//        this.getString("tasks").toLi
//        )
//}

