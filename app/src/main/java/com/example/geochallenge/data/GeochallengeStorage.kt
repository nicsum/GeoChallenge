package com.example.geochallenge.data

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.PostResultsResponce
import com.example.geochallenge.game.Record
import com.example.geochallenge.net.api.GeochallengeApi
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
    ): Single<PostResultsResponce> {
        return api
            .postResults(mode, username, mapId, record.score, record.countTasks)
    }

    override fun getAllRecords(mode: String, mapId: Int): Single<List<Record>> {
        return api.getResults(mode, mapId).map { records ->
            var n = 1
            records.map {
                it.order = n
                n += 1
                it
            }
        }
    }

    override fun getMaps(): Single<List<GameMap>> {
        return api.getMaps()
    }

    override fun getTopAfter(
        position: Int,
        limit: Int,
        mode: String,
        mapId: Int
    ): Single<List<Record>> {
        return api.getTopAfter(position, limit, mode, mapId).map { records ->
            var n = position
            records.map {
                it.order = n
                n += 1
                it
            }
        }
    }

    override fun getTopBefore(
        position: Int,
        limit: Int,
        mode: String,
        mapId: Int
    ): Single<List<Record>> {
        return api.getTopBefore(position, limit, mode, mapId).map { records ->
            var n = position + 2 - records.size
            records.map {
                it.order = n
                n += 1
                it
            }
        }
    }

    override fun getMyRecords(user: String, mode: String, mapId: Int): Single<List<Record>> {
        return api.getMyRecords(user, mode, mapId)
    }

}