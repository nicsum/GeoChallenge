package com.example.geochallenge.di.multiplayer

import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerApi
import com.example.geochallenge.game.multiplayer.MultiplayerApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient


@Module(includes = arrayOf(MultiplayerGameModule::class))
class MultiplayerApiModule {

//    @Provides
//    @MultiplayerGameScope
//    fun provideClient(): OkHttpClient {
//        return OkHttpClient()
//    }

    @Provides
    @MultiplayerGameScope
    fun provideApi(client: OkHttpClient): MultiplayerApi {
        return FirebaseMultiplayerApi(client)
    }


}