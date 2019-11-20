package com.example.geochallenge.game.multiplayer

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.Response

interface MultiplayerApi {

    fun startGame(tasksIds: List<Int>, userId: String): Single<Response>

    fun postAnswer(coordinates: Pair<Double, Double>, userId: String, gameId: String): Completable
}