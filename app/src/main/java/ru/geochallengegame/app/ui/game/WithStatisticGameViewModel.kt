package ru.geochallengegame.app.ui.game

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameStatistic
import ru.geochallengegame.app.game.controlers.GameController


abstract class WithStatisticGameViewModel(val controller: GameController) : BaseGameViewModel() {

    protected var statistic = GameStatistic()
    override fun finishTask() {
        super.finishTask()
        try {
            controller.postGameStats(
                statistic.taskName ?: throw Exception(),
                statistic.distance ?: throw Exception(),
                statistic.level ?: throw Exception(),
                statistic.seconds
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe()
        } catch (e: Exception) {
        }

    }

    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        super.clickedPosition(latitude, longitude, distance)
        statistic.distance = distance
    }

    override fun onStartTask(task: CityTask) {
        super.onStartTask(task)
        statistic = GameStatistic(taskName = task.name, level = currentLevel.value)
    }
}