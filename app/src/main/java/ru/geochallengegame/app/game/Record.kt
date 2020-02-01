package ru.geochallengegame.app.game

import com.google.gson.annotations.SerializedName

class Record(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String,
    @SerializedName("score") var score: Int,
    @SerializedName("count_task") var countTasks: Int,
    var order: Int? = null
) : Comparable<Record> {

    override fun compareTo(other: Record): Int {
        return this.score.compareTo(other.score)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Record

        if (id != other.id) return false
        if (username != other.username) return false
        if (score != other.score) return false
        if (countTasks != other.countTasks) return false
        if (order != other.order) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + username.hashCode()
        result = 31 * result + score
        result = 31 * result + countTasks
        result = 31 * result + (order ?: 0)
        return result
    }


}