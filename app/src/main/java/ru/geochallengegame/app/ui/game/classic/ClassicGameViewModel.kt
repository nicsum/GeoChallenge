package ru.geochallengegame.app.ui.game.classic

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.TaskAnswer
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import ru.geochallengegame.app.ui.game.GameError
import java.util.concurrent.TimeUnit

open class ClassicGameViewModel(
    private val gameController: GameController,
    private val gameMap: GameMap,
    val gameInfo: GameInfo
) : BaseGameViewModel() {

    companion object{
        const val SECONDS_FOR_TASK = 13L
        const val SECONDS_FOR_BONUS = 10L
        const val MAX_POINTS_FOR_DISTANCE = 800.0

    }


    private val maxPointsForLevel = gameInfo.countTaskForLevel *
            (SECONDS_FOR_BONUS.toInt() * 20 + MAX_POINTS_FOR_DISTANCE)

    val secondsPassed = MutableLiveData(0L)
    val neededPoints = MutableLiveData<Int>(neededPointsForNextLevel())

    var points = MutableLiveData<Int>()


//    var pointsForCurrentLevel = MutableLiveData<Int>()

    private var neededPointsForNextLevel = neededPointsForNextLevel()

    private var timerDisposable: Disposable? = null

    override fun onStartTask(task: CityTask) {
        super.onStartTask(task)
        neededPoints.postValue(neededPointsForNextLevel)
        startTimerFromCount()
//        pointsForCurrentLevel.postValue(0)
    }

    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        super.clickedPosition(latitude, longitude, distance)

        //calculate points
        val seconds = secondsPassed.value ?: return
        val pointsForCurrentTask = calculatePoints(seconds, distance)
        points.postValue((points.value ?: 0) + pointsForCurrentTask)
//        pointsForCurrentLevel
//            .postValue((pointsForCurrentLevel.value ?: 0) + pointsForCurrentTask)

        cityTask?.name?.let {
            gameController.postGameStats(it, distance, currentLevel.value!!, seconds.toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe()
        }


        val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))
        taskAnswer.postValue(answer)
        finishTask()

    }

    override fun finishTask() {
        timerDisposable?.dispose()
        super.finishTask()
    }

    private fun startTimerFromCount() {
        timerDisposable?.dispose()

        timerDisposable = Observable
            .intervalRange(0, SECONDS_FOR_TASK + 1, 1, 1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                taskAnswer.postValue(TaskAnswer(cityTask!!))
                finishTask()
            }
            .subscribe {
                secondsPassed.postValue(it)
            }
    }

    override fun levelFinished() {
        super.levelFinished()
        if (neededPointsForNextLevel >=
            points.value ?: 0
        ) {
            finishGame()
        } else {
            neededPointsForNextLevel += neededPointsForNextLevel()
            nextLevel()
        }
    }

    override fun finishGame() {
        if (points.value == 0 || points.value == null) {
            gameResult.postValue(Pair(0, false))
            return
        }
        addDisposable(gameController
            .finishGame(points.value ?: 0, taskCounterGame.value ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                error.postValue(GameError.NONE)
                gameInfo.recordId = it?.id
                val score = if (it.updated) it.score else points.value ?: 0
                gameResult.postValue(Pair(score, it.updated))
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
            })
        )
    }

    override fun getNextTask(): Single<CityTask> {
        return gameController.getNextTask()
    }

    override fun prepareNewLevel(newLevel: Int): Single<Int> {
        return gameController.prepareForLevel(newLevel)
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameController.haveTaskForCurrentLevel()
    }

    private fun getTimeBonus(seconds: Long): Long {
        if (seconds > SECONDS_FOR_BONUS) return 0
        return (SECONDS_FOR_BONUS - seconds) * 20
    }

    private fun neededPointsForNextLevel(): Int {
        return (maxPointsForLevel * 0.5).toInt()
    }

    private fun calculatePoints(seconds: Long, distance: Double): Int {
        val limitDistance = getLimitDistance()
        if (distance >= limitDistance) {
            return 0
        }
        return calculatePointsForDistance(distance) + getTimeBonus(seconds).toInt()
    }

    private fun calculatePointsForDistance(distance: Double): Int {
        val limitDistance = getLimitDistance()
        return ((1 - (distance / limitDistance))
                * MAX_POINTS_FOR_DISTANCE).toInt()
    }

    private fun getLimitDistance() = gameMap.distance ?: DEFAULT_DISTANCE
}