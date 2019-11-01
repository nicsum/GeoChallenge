package com.example.geochallenge


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.example.geochallenge.data.GeoChallengeDataBase
import com.example.geochallenge.data.records.RecordsStorage

class AppDelegate : MultiDexApplication()  {

    companion object{
        lateinit var recordsStorage: RecordsStorage
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        val database = Room.databaseBuilder(this, GeoChallengeDataBase::class.java, "geochallenga_database")
            .fallbackToDestructiveMigration()
            .build()

        recordsStorage = RecordsStorage(database.getDao())
    }
}