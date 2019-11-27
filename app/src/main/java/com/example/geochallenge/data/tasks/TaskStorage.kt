package com.example.geochallenge.data.tasks

import com.example.geochallenge.data.api.GeochallengeApi
import com.example.geochallenge.game.CityTask
import io.reactivex.Single

class TaskStorage(val api: GeochallengeApi) : TaskService {
    override fun getCityTaskById(id: Int): Single<CityTask> {

        return api.getCityTaskById(id)
    }

    override fun getRandomCityTasksByLevel(level: Int, count: Int): Single<List<CityTask>> {
        return api.getRandomCityTasksByLevel(level, count)
    }

    override fun getAllCityTasksByLevel(level: Int): Single<List<CityTask>> {
        return api.getAllCityTasksByLevel(level)
    }

}