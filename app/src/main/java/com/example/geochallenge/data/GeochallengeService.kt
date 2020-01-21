package com.example.geochallenge.data

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.PostResultsResponce
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single

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
        distance: Int
    ): Completable
}