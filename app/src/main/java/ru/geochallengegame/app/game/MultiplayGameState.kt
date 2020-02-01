package ru.geochallengegame.app.game

import java.util.*
import kotlin.collections.ArrayList


data class MultiplayGameState(
    var id: String = "",
    var currentTask: Int = 0,
    var status: Int = 0,
    var tasks: List<Int> = ArrayList(),
    var players: Map<String, List<Double>?> = HashMap()
) {
    fun getIdCurrentTask() = tasks[currentTask]
}

