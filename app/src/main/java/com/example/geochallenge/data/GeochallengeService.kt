package com.example.geochallenge.data

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.Record
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

    fun postRecord(record: Record, mode: String, mapId: Int, userId: String): Single<Record>
    fun getRecords(mode: String, mapId: Int): Single<List<Record>>
    fun getMaps(): Single<List<GameMap>>
}