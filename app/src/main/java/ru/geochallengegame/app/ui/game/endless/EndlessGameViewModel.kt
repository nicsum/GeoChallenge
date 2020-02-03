package ru.geochallengegame.app.ui.game.endless

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.geochallengegame.app.game.CityTask
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.TaskAnswer
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.ui.game.BaseGameViewModel

class EndlessGameViewModel(
    private val gameController: GameController,
    private val gameMap: GameMap,
    val gameInfo: GameInfo
) : BaseGameViewModel() {


    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        super.clickedPosition(latitude, longitude, distance)

        cityTask?.name?.let {
            gameController.postGameStats(it, distance, currentLevel.value!!, -1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorComplete()
                .subscribe()
        }
        val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))
        taskAnswer.postValue(answer)
        finishTask()
    }

    override fun levelFinished() {
        super.levelFinished()
        nextLevel()
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