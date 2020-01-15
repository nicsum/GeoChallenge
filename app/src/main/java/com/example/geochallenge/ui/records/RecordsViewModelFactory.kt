package com.example.geochallenge.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.GeochallengeService
import com.example.geochallenge.data.records.RecordsDataSourceFactory
import com.example.geochallenge.game.GameInfo
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class RecordsViewModelFactory @Inject constructor(
    private val geochallengeService: GeochallengeService,
    private val gameInfo: GameInfo,
    private val recordsDataSourceFactory: RecordsDataSourceFactory,
    private val compositeDisposable: CompositeDisposable
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(
                GeochallengeService::class.java,
                GameInfo::class.java,
                RecordsDataSourceFactory::class.java,
                CompositeDisposable::class.java
            )
            .newInstance(
                geochallengeService,
                gameInfo,
                recordsDataSourceFactory,
                compositeDisposable
            )
    }
}