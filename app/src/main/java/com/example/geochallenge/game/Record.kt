package com.example.geochallenge.game

class Record( var id : Long?= null,
              val points: Int): Comparable<Record> {

    override fun compareTo(other: Record): Int {
        return this.points.compareTo(other.points)
    }
}