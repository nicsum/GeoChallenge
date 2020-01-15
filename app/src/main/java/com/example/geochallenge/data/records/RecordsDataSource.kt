package com.example.geochallenge.data.records

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemKeyedDataSource
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers


class RecordsDataSource(
    private val networkService: GeochallengeService,
    private val compositeDisposable: CompositeDisposable,
    private val gameInfo: GameInfo
) : ItemKeyedDataSource<Int, Record>() {


    var state = MutableLiveData<State>()
    private var retryCompletable: Completable? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Record>
    ) {

        updateState(State.LOADING)

        compositeDisposable.add(
            networkService.getTopAfter(
                params.requestedInitialKey ?: 1,
                params.requestedLoadSize,
                gameInfo.mode,
                gameInfo.mapId
            )
                .subscribe(
                    { records ->
                        updateState(State.DONE)
                        callback.onResult(records, 0, records.size)
                    }, {
                        updateState(State.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    })
        )

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Record>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getTopAfter(
                params.key,
                params.requestedLoadSize,
                gameInfo.mode,
                gameInfo.mapId
            )
                .subscribe(
                    { records ->
                        updateState(State.DONE)
                        callback.onResult(records)
                    }, {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    })
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Record>) {
        updateState(State.LOADING)
        compositeDisposable.add(
            networkService.getTopBefore(
                params.key, params.requestedLoadSize,
                gameInfo.mode,
                gameInfo.mapId
            )
                .subscribe(
                    { records ->
                        updateState(State.DONE)
                        callback.onResult(records)
                    }, {
                        updateState(State.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    })
        )
    }

    override fun getKey(item: Record): Int {
        return item.order ?: throw IllegalArgumentException("item records order is null")
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            compositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    enum class State {
        DONE, LOADING, ERROR
    }
}