package com.example.geochallenge.game.controlers

import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single

class SinglePlayerGameControler(
    val geochallengeService: GeochallengeService,
    val gameInfo: GameInfo,
    val userId: String
) : GameControler {


    lateinit var iteratorTasksForCurrentLevel: Iterator<CityTask>

    override fun getNextTask(): Single<CityTask> {
        return Single.just(iteratorTasksForCurrentLevel.next())
    }

    override fun prepareForLevel(level: Int): Completable {
        return geochallengeService.getRandomCityTasksByLevel(
            level,
            gameInfo.countTaskForLevel,
            gameInfo.mapId
        )
            .flatMapCompletable {
                iteratorTasksForCurrentLevel = it.iterator()
                Completable.complete()
            }
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return iteratorTasksForCurrentLevel.hasNext()
    }

    override fun finishGame(score: Int, countTask: Int): Completable {
        val newRecord = Record(userId = userId, score = score, countTasks = countTask)
        return geochallengeService.postRecord(newRecord, gameInfo.mode, gameInfo.mapId, userId)
    }

}