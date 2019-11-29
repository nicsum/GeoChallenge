package com.example.geochallenge.di.app

import androidx.room.Room
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.data.database.GeoChallengeDao
import com.example.geochallenge.data.database.GeoChallengeDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = arrayOf(AppModule::class))
class DataBaseModule {

    companion object {
        const val DB_NAME = "tasks.db"
    }

    @Provides
    @Singleton
    fun provideDataBase(appDelegate: AppDelegate): GeoChallengeDataBase {
        return Room.databaseBuilder(appDelegate, GeoChallengeDataBase::class.java, "tasks")
            .createFromAsset(DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGeoChallengeDao(dataBase: GeoChallengeDataBase): GeoChallengeDao {
        return dataBase.getDao()
    }

}