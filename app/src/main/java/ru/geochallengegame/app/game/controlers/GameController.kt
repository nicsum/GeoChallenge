package ru.geochallengegame.app.game.controlers

import io.reactivex.Completable
import io.reactivex.Single
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.PostResultsResponce

interface GameController {
    fun getNextTask(): Single<CityTask>
    fun prepareForLevel(level: Int): Single<Int>
    fun haveTaskForCurrentLevel(): Boolean
    fun finishGame(score: Int, countTask: Int): Single<PostResultsResponce>
    fun postGameStats(taskName: String, distance: Double, level: Int?, seconds: Int?): Completable
//    fun finishGameWithPostRecord(score: Int, countTask: Int, postRecord : Boolean): Completable

}