package com.example.geochallenge.data

import androidx.room.Dao
import androidx.room.Query
import com.example.geochallenge.game.CityTask
import io.reactivex.Single


@Dao
interface GeoChallengeDao {

    @Query("select * from cities where level = :level")
    fun getCityTasksByLevel(level: Int): Single<List<CityTask>>

    @Query("select * from cities where level =:level order by random() limit :count")
    fun getRandomCityTasksByLevel(level: Int, count: Int): Single<List<CityTask>>

    @Query("select * from cities where id =:id")
    fun getCityTasksById(id: Int): Single<CityTask>

}