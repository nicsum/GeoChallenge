package com.example.geochallenge.di.menu

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.geochallenge.ui.menu.MenuActivity
import com.example.geochallenge.ui.menu.OnClickMapListener
import com.example.geochallenge.ui.menu.vm.MenuMapsViewModel
import com.example.geochallenge.ui.menu.vm.MenuMapsViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class MenuModule {

    @Provides
    fun provideMenuMapsViewMode(
        activity: MenuActivity,
        factory: MenuMapsViewModelFactory
    ): MenuMapsViewModel {
        return ViewModelProviders.of(activity, factory).get(MenuMapsViewModel::class.java)
    }

    //TODO удалить если не нужно
    @Provides
    fun provideLinearLayoutManager(activity: MenuActivity): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    @Provides
    fun provideListener(activity: MenuActivity): OnClickMapListener {
        return activity
    }
}