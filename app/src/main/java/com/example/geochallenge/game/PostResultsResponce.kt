package com.example.geochallenge.game

import com.google.gson.annotations.SerializedName

class PostResultsResponce(
    @SerializedName("id") var id: Int,
    @SerializedName("score") var score: Int,
    @SerializedName("updated") var updated: Boolean
)