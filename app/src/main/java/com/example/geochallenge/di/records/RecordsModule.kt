package com.example.geochallenge.di.records

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geochallenge.ui.records.RecordsActivity
import com.example.geochallenge.ui.records.RecordsViewModel
import com.example.geochallenge.ui.records.RecordsViewModelFactory
import com.example.geochallenge.ui.utils.LinearLayoutManagerWithSmoothScroller
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable


@Module
class RecordsModule {

    @Provides
    fun provideRecordsViewMode(
        activity: RecordsActivity,
        factory: RecordsViewModelFactory
    ): RecordsViewModel {
        return ViewModelProvider(activity, factory).get(RecordsViewModel::class.java)
    }

    @Provides
    fun provideLinearLayoutManager(activity: RecordsActivity): LinearLayoutManager {
        return LinearLayoutManagerWithSmoothScroller(activity)
    }

    @Provides
    @RecordsScope
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

}

