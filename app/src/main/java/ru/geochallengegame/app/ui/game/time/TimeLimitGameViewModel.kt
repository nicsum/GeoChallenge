package ru.geochallengegame.app.ui.game.time

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Completable
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
import ru.geochallengegame.app.ui.game.GameError
import ru.geochallengegame.app.ui.game.WithStatisticGameViewModel
import java.util.concurrent.TimeUnit

class TimeLimitGameViewModel(
    private val gameController: GameController,
    val gameMap: GameMap,
    val gameInfo: GameInfo
) : WithStatisticGameViewModel(gameController) {

    companion object {
        const val DEFAULT_COUNT_TIMER: Long = 30
    }

//    val stillHaveTime = MutableLiveData<Long>()
//    val secondsPassed = MutableLiveData<Long>().also { it.value = 0L }

    private var timeLeft =
        DEFAULT_COUNT_TIMER

    private var timerDisposable: Disposable? = null

    val timer =
        MutableLiveData<Pair<Long, Long>>(0L to DEFAULT_COUNT_TIMER)


    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
         super.clickedPosition(latitude, longitude, distance)
         //calculate time
         val timeBonus = getTimeBonus(distance)
         val resultTime = timer.value!!.second - timer.value!!.first + timeBonus
        val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))

         taskAnswer.postValue(answer)

         if (resultTime <= 0) {
             finishGame()
             timeLeft = 0
         } else {
             timeLeft = resultTime
             addDisposable(Completable.complete()
                 .subscribeOn(AndroidSchedulers.mainThread())
                 .observeOn(AndroidSchedulers.mainThread())
                 .delay(2, TimeUnit.SECONDS)
                 .subscribe {
                     nextTask()
                 })
             finishTask()
         }
     }


    override fun finishTask() {
        timerDisposable?.dispose()
        super.finishTask()
    }

     override fun levelFinished() {
         super.levelFinished()
         nextLevel()
     }

    private fun startTimer(count: Long) {
        timerDisposable?.dispose()
        timerDisposable = Observable
            .intervalRange(0, count + 1, 1, 1, TimeUnit.SECONDS)
            .doOnComplete(this::finishGame)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                timer.postValue(0L to count)
            }
            .subscribe{
                statistic.seconds = it.toInt()
//                val timeLeft = timer.value!!.first + 1
                timer.postValue(it to count)
            }

    }

    private fun getTimeBonus(distance: Double): Int {
        val percent = distance / getLimitDistance()

        return when {
            percent <= 0.1 -> 9
            percent <= 0.2 -> 8
            percent <= 0.4 -> 7
            percent <= 0.6 -> 6
            percent <= 0.8 -> 5
            percent <= 1 -> 0
            percent <= 1.2 -> -2
            percent <= 1.4 -> -3
            percent <= 1.6 -> -4
            percent <= 1.8 -> -5
            percent <= 2 -> -10
            else -> -10
        }
    }

    override fun getNextTask(): Single<CityTask> {
        return gameController
            .getNextTask()
            .doOnSuccess { startTimer(timeLeft) }
    }

    override fun prepareNewLevel(newLevel: Int): Single<Int> {
        return gameController.prepareForLevel(newLevel)
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return gameController.haveTaskForCurrentLevel()
    }

    override fun finishGame() {
        timerDisposable?.dispose()
        if (!isTaskCompleted.value!!) finishTask()
        if (taskCounterGame.value == 1 || taskCounterGame.value == null) {
            gameResult.postValue(Pair(0, false))
            super.finishGame()
            return
        }
        addDisposable(gameController
            .finishGame(taskCounterGame.value?.minus(1) ?: 0, taskCounterGame.value ?: 0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                super.finishGame()
                error.postValue(GameError.NONE)
                gameInfo.recordId = it?.id
                val score = if (it.updated) it.score else taskCounterGame.value ?: 0
                gameResult.postValue(Pair(score, it.updated))
            }, {
                error.postValue(GameError.FINISH_GAME_ERROR)
            })
        )
    }

    private fun getLimitDistance() = gameMap.distance ?: DEFAULT_DISTANCE
}