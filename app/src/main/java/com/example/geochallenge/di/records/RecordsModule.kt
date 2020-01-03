package com.example.geochallenge.di.records

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geochallenge.ui.records.RecordsActivity
import com.example.geochallenge.ui.records.RecordsViewModel
import com.example.geochallenge.ui.records.RecordsViewModelFactory
import com.example.geochallenge.ui.utils.LinearLayoutManagerWithSmoothScroller
import dagger.Module
import dagger.Provides


@Module
class RecordsModule {

    @Provides
    fun provideRecordsViewMode(
        activity: RecordsActivity,
        factory: RecordsViewModelFactory
    ): RecordsViewModel {
        return ViewModelProviders.of(activity, factory).get(RecordsViewModel::class.java)
    }

    @Provides
    fun provideLinearLayoutManager(activity: RecordsActivity): LinearLayoutManager {
        return LinearLayoutManagerWithSmoothScroller(activity)
    }
}

