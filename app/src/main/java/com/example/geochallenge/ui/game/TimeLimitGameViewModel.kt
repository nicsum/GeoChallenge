package com.example.geochallenge.ui.game

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

 class TimeLimitGameViewModel : SimpleGameViewModel() {

        companion object {
        const val COUNT_TIMER : Long = 30

    }

    val stillHaveTime = MutableLiveData<Long>()
    var stillHaveTimeLong = COUNT_TIMER
    var timerDisposable: Disposable? = null

    override fun newGame() {
        super.newGame()
        startTimerFromCount(COUNT_TIMER)
        distance.observeForever{
            val timeBonus = getTimeBonus(it)
            startTimerFromCount(stillHaveTimeLong + timeBonus)
        }
    }


    fun startTimerFromCount(count : Long ){

        timerDisposable?.dispose()

        timerDisposable = Observable
            .intervalRange(1, count + 1,1,1, TimeUnit.SECONDS)
            .map { count + 1 - it  }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete(this::finishGame)
            .subscribe{
                stillHaveTime.postValue( it)
                stillHaveTimeLong = it
            }
    }


    private fun getTimeBonus(distance: Int) = when {

        distance <= 100 -> 30
        distance <= 200 -> 20
        distance <= 400 -> 10
        distance <= 800 -> 5
        else -> 0

    }

}