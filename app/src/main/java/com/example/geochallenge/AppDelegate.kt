package com.example.geochallenge

import android.app.Application
import androidx.room.Room
import com.example.geochallenge.data.GeoChallengeDataBase
import com.example.geochallenge.data.records.RecordsStorage

class AppDelegate : Application() {

    companion object{
        lateinit var recordsStorage: RecordsStorage
    }



    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(this, GeoChallengeDataBase::class.java, "geochallenga_database")
            .fallbackToDestructiveMigration()
            .build()

        recordsStorage = RecordsStorage(database.getDao())
    }
}