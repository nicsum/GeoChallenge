package com.example.geochallenge.data

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single

interface GeochallengeService {

    fun getAllCityTasksByLevel(level: Int, mapId: Int): Single<List<CityTask>>

    fun getRandomCityTasksByLevel(level: Int, count: Int, mapId: Int): Single<List<CityTask>>

    fun getCityTaskById(id: Int, mapId: Int): Single<CityTask>

    fun postRecord(record: Record, mode: String, mapId: Int, userId: String): Completable
    fun getRecords(mode: String, mapId: Int): Single<List<Record>>
    fun getMaps(): Single<List<GameMap>>
}