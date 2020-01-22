package com.example.geochallenge.game.controlers

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.PostResultsResponce
import io.reactivex.Completable
import io.reactivex.Single

interface GameControler {
    fun getNextTask(): Single<CityTask>
    fun prepareForLevel(level: Int): Completable
    fun haveTaskForCurrentLevel(): Boolean
    fun finishGame(score: Int, countTask: Int): Single<PostResultsResponce>
    fun postGameStats(taskName: String, distance: Double): Completable
//    fun finishGameWithPostRecord(score: Int, countTask: Int, postRecord : Boolean): Completable

}