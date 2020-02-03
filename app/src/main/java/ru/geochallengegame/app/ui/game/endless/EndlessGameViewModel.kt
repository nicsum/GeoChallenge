package ru.geochallengegame.app.ui.game.endless

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.TaskAnswer
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.ui.game.WithStatisticGameViewModel

class EndlessGameViewModel(
    private val gameController: GameController,
    private val gameMap: GameMap,
    val gameInfo: GameInfo
) : WithStatisticGameViewModel(gameController) {


    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        super.clickedPosition(latitude, longitude, distance)
        val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))
        taskAnswer.postValue(answer)
        finishTask()
    }

    override fun levelFinished() {
        super.levelFinished()
        nextLevel()
    }

    override fun finishGame() {
        if (!isTaskCompleted.value!!) finishTask()
        super.finishGame()
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
}