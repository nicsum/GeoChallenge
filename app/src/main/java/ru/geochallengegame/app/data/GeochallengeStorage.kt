package ru.geochallengegame.app.data

import io.reactivex.Completable
import io.reactivex.Single
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.PostResultsResponce
import ru.geochallengegame.app.game.Record
import ru.geochallengegame.app.net.api.GeochallengeApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeochallengeStorage @Inject constructor(val api: GeochallengeApi) :
    GeochallengeService {


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

    override fun getMaps(mode: String): Single<List<GameMap>> {
        return api.getMaps(mode)
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

    override fun postStats(
        mapId: Int?,
        taskName: String?,
        distance: Double?,
        mode: String?,
        level: Int?,
        seconds: Int?,
        username: String?
    ): Completable {
        return api.postStats(mapId, taskName, distance, level, seconds, username, mode)
    }

}