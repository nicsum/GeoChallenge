package com.example.geochallenge.data.api

import com.example.geochallenge.game.CityTask
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface GeochallengeApi {

    //TODO нормально сделай map аргумент

    @GET("getCityTasksByLevel")
    fun getAllCityTasksByLevel(
        @Query("level") level: Int,
        @Query("map") map: String = "russia"
    ): Single<List<CityTask>>

    @GET("getRandomCityTasksByLevel")
    fun getRandomCityTasksByLevel(
        @Query("level") level: Int,
        @Query("limit") limit: Int,
        @Query("map") map: String = "russia"
    ): Single<List<CityTask>>

    @GET("/getCityTasksById")
    fun getCityTaskById(
        @Query("cityid") id: Int,
        @Query("map") map: String = "russia"
    ): Single<CityTask>
}