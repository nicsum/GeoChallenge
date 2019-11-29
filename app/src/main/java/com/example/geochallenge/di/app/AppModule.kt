package com.example.geochallenge.di.app

import com.example.geochallenge.AppDelegate
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.data.GeochallengeStorage
import com.example.geochallenge.data.api.GeochallengeApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = arrayOf(NetworkModule::class, DataBaseModule::class))
class AppModule(private val appDelegate: AppDelegate) {


    @Provides
    @Singleton
    fun provideApp() = appDelegate

    @Provides
    @Singleton
    fun provideStorage(api: GeochallengeApi): GeochallengeStorage {
        return GeochallengeStorage(api)
    }


    @Provides
    @Singleton
    fun provideTaskService(storage: GeochallengeStorage): GeochallengeService {
        return storage
    }

}



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
