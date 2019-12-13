package com.example.geochallenge.di.app

import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.data.GeochallengeStorage
import dagger.Binds
import dagger.Module


@Module(includes = arrayOf(NetworkModule::class, DataBaseModule::class))
abstract class AppModule {


//    @Provides
//    @Singleton
//    fun provideApp() = appDelegate


//    @Provides
//    @Singleton
//    fun provideStorage(api: GeochallengeApi): GeochallengeStorage {
//        return GeochallengeStorage(api)
//    }


//    @Provides
////    @Singleton
//    fun provideTaskService(storage: GeochallengeStorage): GeochallengeService {
//        return storage
//    }

    @Binds
    abstract fun provideTaskService(storage: GeochallengeStorage): GeochallengeService

}

