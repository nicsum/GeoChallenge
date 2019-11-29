package com.example.geochallenge.game

import androidx.room.ColumnInfo

class Record(
    var id: Int? = null,
    @ColumnInfo(name = "user_id") var userId: String,
    @ColumnInfo(name = "score") var score: Int,
    @ColumnInfo(name = "count_task") var countTasks: Int

) : Comparable<Record> {

    override fun compareTo(other: Record): Int {
        return this.score.compareTo(other.score)
    }
}