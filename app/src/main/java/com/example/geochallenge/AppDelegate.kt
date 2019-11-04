package com.example.geochallenge


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.geochallenge.data.GeoChallengeDataBase
import com.example.geochallenge.data.tasks.TaskStorage

class AppDelegate : MultiDexApplication()  {

    companion object{

//        val MIGRATION_1_2  = object: Migration(1,2){
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE cache ADD COLUMN id INTEGER primary KEY AUTOINCREMENT")
//            }
//
//
//        }
        lateinit var taskStorage: TaskStorage
        lateinit var INSTANCE: AppDelegate
        const val DB_NAME = "tasks.db"

    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        val database = Room.databaseBuilder(this, GeoChallengeDataBase::class.java, "tasks")
            .createFromAsset(DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
        taskStorage = TaskStorage(database.getDao())

    }

}