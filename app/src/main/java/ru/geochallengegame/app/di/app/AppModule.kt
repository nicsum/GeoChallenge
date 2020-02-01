package ru.geochallengegame.app.di.app

import dagger.Binds
import dagger.Module
import ru.geochallengegame.app.data.GeochallengeService
import ru.geochallengegame.app.data.GeochallengeStorage


@Module
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

