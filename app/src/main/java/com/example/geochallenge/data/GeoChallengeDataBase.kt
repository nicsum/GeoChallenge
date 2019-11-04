package com.example.geochallenge.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.geochallenge.game.CityTask


@Database(entities = arrayOf(CityTask::class), version = 1)
abstract class GeoChallengeDataBase : RoomDatabase() {

    abstract fun getDao(): GeoChallengeDao
}