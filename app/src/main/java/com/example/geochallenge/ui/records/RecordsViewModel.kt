package com.example.geochallenge.ui.records

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RecordsViewModel(val geochallengeService: GeochallengeService, val gameInfo: GameInfo) :
    ViewModel() {

    val records = MutableLiveData<List<Record>>()

    fun showRecords() {
        geochallengeService
            .getRecords(gameInfo.mode, gameInfo.mapId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                records.postValue(it)
            }, {
                Log.d("RecordsViewModel", it.message)
            })

    }


}