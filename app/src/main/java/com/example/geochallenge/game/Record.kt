package com.example.geochallenge.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "records")
class Record(@PrimaryKey(autoGenerate = true) var id : Long?= null,
             @ColumnInfo(name = "points") val points: Int): Comparable<Record> {
    override fun compareTo(other: Record): Int {
        return this.points.compareTo(other.points)
    }
}