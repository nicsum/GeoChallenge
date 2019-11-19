package com.example.geochallenge.game.multiplayer

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List
import kotlin.collections.Map


data class GameState(
    var id: String = "",
    var currentTask: Int = 0,
    var status: Int = 0,
    var tasks: List<Int> = ArrayList(),
    var players: Map<String, Pair<Double, Double>> = HashMap()
) {
    fun getIdCurrentTask() = tasks[currentTask]
}

