package com.example.geochallenge.game.controlers

import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.PostResultsResponce
import com.example.geochallenge.game.Record
import com.example.geochallenge.user.UserDataRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class SinglePlayerGameControler @Inject constructor(
    val geochallengeService: GeochallengeService,
    val gameInfo: GameInfo,
    val userDataRepository: UserDataRepository
) : GameControler {


    lateinit var iteratorTasksForCurrentLevel: Iterator<CityTask>

    override fun getNextTask(): Single<CityTask> {
        return Single.just(iteratorTasksForCurrentLevel.next())
    }

    override fun prepareForLevel(level: Int): Single<Int> {
        return geochallengeService.getRandomCityTasksByLevel(
            level,
            gameInfo.countTaskForLevel,
            gameInfo.mapId,
            gameInfo.tasksLang
        )
            .flatMap {
                iteratorTasksForCurrentLevel = it.iterator()
                Single.just(it.size)
            }
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return iteratorTasksForCurrentLevel.hasNext()
    }


    override fun finishGame(score: Int, countTask: Int): Single<PostResultsResponce> {
        val username = userDataRepository.username
        val newRecord = Record(username = username, score = score, countTasks = countTask)
        return geochallengeService.postRecord(newRecord, gameInfo.mode, gameInfo.mapId, username)
    }

    override fun postGameStats(taskName: String, distance: Double): Completable {
        return geochallengeService.postStats(gameInfo.mapId, taskName, distance)
    }


//    override fun finishGameWithPostRecord(score: Int, countTask: Int): Completable {
//        if (score == 0) return Completable.complete()
//        val userId = userDataRepository.uid
//        val newRecord = Record(userId = userId, score = score, countTasks = countTask)
//        return geochallengeService.postRecord(newRecord, gameInfo.mode, gameInfo.mapId, userId)
//    }

}