package ru.geochallengegame.app.di.records

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import ru.geochallengegame.app.ui.records.RecordsActivity
import ru.geochallengegame.app.ui.records.RecordsViewModel
import ru.geochallengegame.app.ui.records.RecordsViewModelFactory
import ru.geochallengegame.app.ui.utils.LinearLayoutManagerWithSmoothScroller


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


}

