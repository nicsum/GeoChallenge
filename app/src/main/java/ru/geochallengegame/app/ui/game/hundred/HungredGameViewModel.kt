package ru.geochallengegame.app.ui.game.hundred

import com.google.android.gms.maps.model.LatLng
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.TaskAnswer
import ru.geochallengegame.app.game.controlers.GameController
import ru.geochallengegame.app.ui.game.classic.ClassicGameViewModel

class HungredGameViewModel(
    gameController: GameController,
    gameMap: GameMap,
    gameInfo: GameInfo
) : ClassicGameViewModel(gameController, gameMap, gameInfo) {

    companion object {
        const val FATAL_DISTANCE = 100
    }

    override fun clickedPosition(latitude: Double, longitude: Double, distance: Double) {
        if (distance > FATAL_DISTANCE) {
            this.distance.postValue(distance)
            statistic.distance = distance
            val answer = TaskAnswer(cityTask!!, LatLng(latitude, longitude))
            taskAnswer.postValue(answer)
            finishGame()
        } else super.clickedPosition(latitude, longitude, distance)
    }
}