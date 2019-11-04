package com.example.geochallenge.data

import androidx.room.*
import com.example.geochallenge.game.CityTask
import io.reactivex.Single


@Dao
interface GeoChallengeDao {

    @Query("select * from cities where level = :level")
    fun getCityTasksByLevel(level:Int): Single<MutableList<CityTask>>

    @Query("select * from cities where level =:level order by random() limit :count")
    fun getRandomCityTasksByLevel(level: Int , count: Int): Single<MutableList<CityTask>>


}