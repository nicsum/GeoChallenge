package ru.geochallengegame.app.game.controlers

import io.reactivex.Completable
import io.reactivex.Single
import ru.geochallengegame.app.data.GeochallengeService
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.PostResultsResponce
import ru.geochallengegame.app.game.Record
import ru.geochallengegame.app.user.UserDataRepository
import javax.inject.Inject

class SinglePlayerGameController @Inject constructor(
    private val geochallengeService: GeochallengeService,
    val gameInfo: GameInfo,
    private val userDataRepository: UserDataRepository
) : GameController {


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

    override fun postGameStats(
        taskName: String,
        distance: Double,
        level: Int?,
        seconds: Int?
    ): Completable {
        val username = userDataRepository.username
        return geochallengeService.postStats(
            gameInfo.mapId,
            taskName,
            distance,
            gameInfo.mode,
            level,
            seconds,
            username
        )
    }
//    override fun finishGameWithPostRecord(score: Int, countTask: Int): Completable {
//        if (score == 0) return Completable.complete()
//        val userId = userDataRepository.uid
//        val newRecord = Record(userId = userId, score = score, countTasks = countTask)
//        return geochallengeService.postRecord(newRecord, gameInfo.mode, gameInfo.mapId, userId)
//    }

}