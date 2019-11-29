package com.example.geochallenge.game

import java.io.Serializable

data class GameInfo(
    val mode: String,
    val mapId: Int,
    val countTaskForLevel: Int
) : Serializable