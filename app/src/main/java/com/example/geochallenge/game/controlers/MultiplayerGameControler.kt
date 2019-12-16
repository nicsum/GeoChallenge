package com.example.geochallenge.game.controlers

import com.example.geochallenge.game.CityTask
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class MultiplayerGameControler @Inject constructor() : GameControler {


    var haveNextTask = true

    lateinit var e: SingleEmitter<CityTask>

    //костыльный метод какой-то: странно что вьюмодель постит таск чтобы потом его юзать
    // хочется чтобы левелпровайдер поставлял таски сам, независимо от вьюмодели
    fun postTask(task: CityTask) {
        e.onSuccess(task)
    }

    override fun getNextTask(): Single<CityTask> {
        return Single.create<CityTask> { e = it }
    }

    override fun prepareForLevel(level: Int): Completable {
        return Completable.complete()
    }

    override fun haveTaskForCurrentLevel(): Boolean {
        return haveNextTask
    }

    override fun finishGame(score: Int, countTask: Int): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}