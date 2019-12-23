package com.example.geochallenge.data

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.Record
import com.example.geochallenge.net.api.GeochallengeApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeochallengeStorage @Inject constructor(val api: GeochallengeApi) : GeochallengeService {


    override fun getCityTaskById(id: Int, mapId: Int, lang: String): Single<CityTask> {

        return api.getCityTaskById(id, mapId, lang)
    }

    override fun getRandomCityTasksByLevel(
        level: Int,
        count: Int,
        mapId: Int,
        lang: String
    ): Single<List<CityTask>> {
        return api.getRandomCityTasksByLevel(level, count, mapId, lang)
    }

    override fun getAllCityTasksByLevel(
        level: Int,
        mapId: Int,
        lang: String
    ): Single<List<CityTask>> {
        return api.getAllCityTasksByLevel(level, mapId, lang)
    }

    override fun postRecord(
        record: Record,
        mode: String,
        mapId: Int,
        username: String
    ): Completable {
        return api
            .postResults(mode, username, mapId, record.score, record.countTasks)
    }

    override fun getRecords(mode: String, mapId: Int): Single<List<Record>> {
        return api.getResults(mode, mapId)
    }

    override fun getMaps(): Single<List<GameMap>> {
        return api.getMaps()
    }

}