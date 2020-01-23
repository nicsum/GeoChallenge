package com.example.geochallenge.ui.records

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.data.records.RecordsDataSourceFactory
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RecordsViewModel(
    private val geochallengeService: GeochallengeService,
    private val gameInfo: GameInfo,
    private val recordsDataSourceFactory: RecordsDataSourceFactory,
    val compositeDisposable: CompositeDisposable

) :
    ViewModel() {


    val records = MutableLiveData<List<Record>>()
    val loadingIsVisible = MutableLiveData<Boolean>()

    fun loadRecords() {
        geochallengeService
            .getAllRecords(gameInfo.mode, gameInfo.mapId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { loadingIsVisible.postValue(true) }
            .doFinally { loadingIsVisible.postValue(false) }
            .subscribe({
                records.postValue(it)
            }, {
                Log.d("RecordsViewModel", it.message)
            })
    }


//    var recordsList : LiveData<PagedList<Record>>
//
//    init {
//        val config =  PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setInitialLoadSizeHint(100 *2 )
//            .setPageSize(100)
//            .build()
//
//        recordsList = LivePagedListBuilder<Int, Record>(recordsDataSourceFactory, config).build()
//
//    }
//    fun getState(): LiveData<RecordsDataSource.State> = Transformations.switchMap<RecordsDataSource,
//            RecordsDataSource.State>(recordsDataSourceFactory.recordsDataSourceLiveData, RecordsDataSource::state)
//
//    fun retry(){
//        recordsDataSourceFactory.recordsDataSourceLiveData.value?.retry()
//    }
//
//    fun listIsEmpty(): Boolean {
//        return recordsList.value?.isEmpty() ?: true
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        compositeDisposable.dispose()
//    }

}