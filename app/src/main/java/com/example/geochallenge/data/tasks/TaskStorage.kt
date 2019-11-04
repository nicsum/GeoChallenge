package com.example.geochallenge.data.tasks

import com.example.geochallenge.data.GeoChallengeDao
import com.example.geochallenge.game.CityTask
import io.reactivex.Single

class TaskStorage(val dao: GeoChallengeDao) : TaskService {

    override fun getRandomCityTasksByLevel(level: Int, count: Int): Single<MutableList<CityTask>> {
        return dao.getRandomCityTasksByLevel(level, count)
    }


    override fun getAllCityTasksByLevel(level: Int): Single<MutableList<CityTask>> {
        return dao.getCityTasksByLevel(level)
    }

}