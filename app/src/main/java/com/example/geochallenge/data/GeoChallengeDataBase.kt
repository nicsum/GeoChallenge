package com.example.geochallenge.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geochallenge.game.Record


@Database(entities = arrayOf(Record::class), version = 1)
abstract class GeoChallengeDataBase : RoomDatabase() {

    abstract fun getDao(): GeoChallengeDao
}