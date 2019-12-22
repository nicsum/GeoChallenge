package com.example.geochallenge.di.app

import android.util.Log
import com.example.geochallenge.BuildConfig.API_URL
import com.example.geochallenge.net.api.GeochallengeApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module(includes = arrayOf(DataBaseModule::class))
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.i("GeochallengeNetwork", message)
            }
        }

        val loggingInterceptor = HttpLoggingInterceptor(logger)
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {

        val builder = OkHttpClient()
            .newBuilder()
            .addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GeochallengeApi {
        return retrofit.create(GeochallengeApi::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideApiService(dao: GeoChallengeDao): GeochallengeApi {
//        val mc = MockClassicRussianGeochallengeApi
//        mc.dao = dao
//        return mc
//    }

}