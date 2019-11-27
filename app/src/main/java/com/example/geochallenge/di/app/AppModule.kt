package com.example.geochallenge.di.app

import com.example.geochallenge.AppDelegate
import com.example.geochallenge.data.api.GeochallengeApi
import com.example.geochallenge.data.tasks.TaskService
import com.example.geochallenge.data.tasks.TaskStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = arrayOf(NetworkModule::class))
class AppModule(private val appDelegate: AppDelegate) {

    companion object {
        const val DB_NAME = "tasks.db"
    }

    @Provides
    @Singleton
    fun provideApp() = appDelegate

//    @Provides
//    @Singleton
//    fun provideDataBase(): GeoChallengeDataBase {
//        return Room.databaseBuilder(appDelegate, GeoChallengeDataBase::class.java, "tasks")
//            .createFromAsset(DB_NAME)
//            .fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideGeoChallengeDao(dataBase: GeoChallengeDataBase): GeoChallengeDao {
//        return dataBase.getDao()
//    }


    @Provides
    @Singleton
    fun provideStorage(api: GeochallengeApi): TaskStorage {
        return TaskStorage(api)
    }

    @Provides
    @Singleton
    fun provideTaskService(storage: TaskStorage): TaskService {
        return storage
    }

}