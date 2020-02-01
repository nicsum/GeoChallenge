package ru.geochallengegame.app.game

import java.io.Serializable

data class GameInfo(
    val mode: String,
    val mapId: Int,
    val countTaskForLevel: Int,
    val tasksLang: String,
    var recordId: Int? = null
) : Serializable