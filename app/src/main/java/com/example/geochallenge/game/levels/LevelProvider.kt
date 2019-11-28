package com.example.geochallenge.game.levels

import com.example.geochallenge.game.CityTask
import io.reactivex.Completable
import io.reactivex.Single

interface LevelProvider {
    fun getNextTask(): Single<CityTask>
    fun prepareForLevel(level: Int): Completable
    fun haveTaskForCurrentLevel(): Boolean

}