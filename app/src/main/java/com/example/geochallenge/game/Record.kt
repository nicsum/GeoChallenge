package com.example.geochallenge.game

import com.google.gson.annotations.SerializedName

class Record(
    var id: Int? = null,
    @SerializedName("username") var userId: String,
    @SerializedName("score") var score: Int,
    @SerializedName("count_task") var countTasks: Int

) : Comparable<Record> {

    override fun compareTo(other: Record): Int {
        return this.score.compareTo(other.score)
    }
}