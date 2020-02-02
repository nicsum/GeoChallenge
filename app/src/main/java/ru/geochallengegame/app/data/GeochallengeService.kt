package ru.geochallengegame.app.data

import io.reactivex.Completable
import io.reactivex.Single
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.PostResultsResponce
import ru.geochallengegame.app.game.Record

interface GeochallengeService {

    fun getAllCityTasksByLevel(level: Int, mapId: Int, lang: String): Single<List<CityTask>>

    fun getRandomCityTasksByLevel(
        level: Int,
        count: Int,
        mapId: Int,
        lang: String
    ): Single<List<CityTask>>

    fun getCityTaskById(id: Int, mapId: Int, lang: String): Single<CityTask>

    fun postRecord(
        record: Record,
        mode: String,
        mapId: Int,
        username: String
    ): Single<PostResultsResponce>

    fun getAllRecords(mode: String, mapId: Int): Single<List<Record>>
    fun getMaps(): Single<List<GameMap>>

    fun getTopAfter(
        position: Int,
        limit: Int,
        mode: String,
        mapId: Int
    ): Single<List<Record>>


    fun getTopBefore(
        position: Int,
        limit: Int,
        mode: String,
        mapId: Int
    ): Single<List<Record>>

    fun getMyRecords(
        user: String,
        mode: String,
        mapId: Int
    ): Single<List<Record>>

    fun postStats(
        mapId: Int,
        taskName: String,
        distance: Double,
        level: Int,
        seconds: Int,
        username: String
    ): Completable
}