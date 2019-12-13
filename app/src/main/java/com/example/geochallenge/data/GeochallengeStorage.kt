package com.example.geochallenge.data

import com.example.geochallenge.data.api.GeochallengeApi
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeochallengeStorage @Inject constructor(val api: GeochallengeApi) : GeochallengeService {


    override fun getCityTaskById(id: Int, mapId: Int): Single<CityTask> {

        return api.getCityTaskById(id, mapId)
    }

    override fun getRandomCityTasksByLevel(
        level: Int,
        count: Int,
        mapId: Int
    ): Single<List<CityTask>> {
        return api.getRandomCityTasksByLevel(level, count, mapId)
    }

    override fun getAllCityTasksByLevel(level: Int, mapId: Int): Single<List<CityTask>> {
        return api.getAllCityTasksByLevel(level, mapId)
    }

    override fun postRecord(record: Record, mode: String, mapId: Int, userId: String): Completable {
        return api
            .postResults(mode, userId, mapId, record.score, record.countTasks)
    }

    override fun getRecords(mode: String, mapId: Int): Single<List<Record>> {
        return api.getResults(mode, mapId)
    }

}