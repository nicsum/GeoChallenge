package com.example.geochallenge.data.api

import com.example.geochallenge.game.CityTask
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*


interface GeochallengeApi {


    @GET("getCityTasksByLevel")
    fun getAllCityTasksByLevel(
        @Query("level") level: Int,
        @Query("map_id") mapId: Int
    ): Single<List<CityTask>>

    @GET("getRandomCityTasksByLevel")
    fun getRandomCityTasksByLevel(
        @Query("level") level: Int,
        @Query("limit") limit: Int,
        @Query("map_id") mapId: Int
    ): Single<List<CityTask>>

    @GET("/getCityTasksById")
    fun getCityTaskById(
        @Query("cityid") id: Int,
        @Query("map_id") mapId: Int
    ): Single<CityTask>

    @FormUrlEncoded
    @POST("/postResults")
    fun postResults(
        @Field("mode") mode: String,
        @Field("user_id") user: String,
        @Field("map_id") mapId: Int,
        @Field("score") score: Int,
        @Field("count_task") countTasks: Int
    ): Completable

    @GET("/getResults")
    fun getResults(
        @Query("mode") mode: String,
        @Query("mapId") mapId: Int
    ): Single<List<Record>>

}