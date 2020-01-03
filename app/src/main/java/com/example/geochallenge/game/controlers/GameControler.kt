package com.example.geochallenge.game.controlers

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single

interface GameControler {
    fun getNextTask(): Single<CityTask>
    fun prepareForLevel(level: Int): Completable
    fun haveTaskForCurrentLevel(): Boolean
    fun finishGame(score: Int, countTask: Int): Single<Record>
//    fun finishGameWithPostRecord(score: Int, countTask: Int, postRecord : Boolean): Completable

}