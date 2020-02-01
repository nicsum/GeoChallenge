package ru.geochallengegame.app.ui.menu

import ru.geochallengegame.app.game.GameMap

interface OnClickMapListener {

    fun onClickGameMap(map: GameMap, lang: String)
}