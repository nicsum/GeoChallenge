package com.example.geochallenge.data.tasks

import com.example.geochallenge.game.CityTask
import io.reactivex.Single

interface TaskService {

    fun getAllCityTasksByLevel(level: Int): Single<List<CityTask>>

    fun getRandomCityTasksByLevel(level: Int, count: Int): Single<List<CityTask>>

    fun getCityTaskById(id: Int): Single<CityTask>
}