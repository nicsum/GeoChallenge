package com.example.geochallenge.ui.menu.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameMap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MenuMapsViewModel(private val geochallengeService: GeochallengeService) : ViewModel() {

    val maps = MutableLiveData<List<GameMap>>()

    fun loadMaps() {
        geochallengeService
            .getMaps()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                maps.postValue(it)
            }, {
                Log.d("MenuMapsViewModel", it.message)
            })
    }
}