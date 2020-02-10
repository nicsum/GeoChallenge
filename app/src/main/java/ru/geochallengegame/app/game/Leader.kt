package ru.geochallengegame.app.game

import com.google.gson.annotations.SerializedName

data class Leader(
    @SerializedName("username") var username: String,
    @SerializedName("score") var score: Int,
    @SerializedName("position") var position: Int
)