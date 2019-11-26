package com.example.geochallenge.game.multiplayer

import android.net.Uri
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class FirebaseMultiplayerApi(val client: OkHttpClient) :
    MultiplayerApi {


    companion object {
        val baseUrlStartNewGame =
            "https://europe-west1-geochallenge.cloudfunctions.net/multiplayerGame"
        val baseUrlAddAnswer = "https://europe-west1-geochallenge.cloudfunctions.net/addAnswer"
    }



    override fun postAnswer(
        coordinates: Pair<Double, Double>,
        userId: String,
        gameId: String
    ): Completable {
        val url = getUrlAddAnswer(coordinates, userId, gameId)
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

    override fun startGame(tasksIds: List<Int>, userId: String): Single<Response> {
        return Single.defer {
            try {
                val url = getUrlStartNewGame(tasksIds.toString(), userId)
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                Single.just(response)
            } catch (e: IOException) {
                Single.error<Response>(e)
            }
        }

    }

    private fun getUrlStartNewGame(tasks: String, userId: String): String {

        return Uri.Builder().encodedPath(baseUrlStartNewGame)
            .appendQueryParameter("userId", userId)
            .appendQueryParameter("tasks", tasks)
            .toString()
    }

    private fun getUrlAddAnswer(
        coordinates: Pair<Double, Double>,
        userId: String,
        gameId: String
    ): String {

        return Uri.Builder().encodedPath(baseUrlAddAnswer)
            .appendQueryParameter("userId", userId)
            .appendQueryParameter("gameId", gameId)
            .appendQueryParameter("geoPoint", coordinates.toList().toString())
            .toString()
    }

}