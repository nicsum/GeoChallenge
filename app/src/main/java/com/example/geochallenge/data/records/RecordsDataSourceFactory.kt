package com.example.geochallenge.data.records

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecordsDataSourceFactory @Inject constructor(
    private val networkService: GeochallengeService,
    private val compositeDisposable: CompositeDisposable,

    private val gameInfo: GameInfo
) : DataSource.Factory<Int, Record>() {

    val recordsDataSourceLiveData = MutableLiveData<RecordsDataSource>()
    override fun create(): DataSource<Int, Record> {

        val dataSource = RecordsDataSource(networkService, compositeDisposable, gameInfo)
        recordsDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}