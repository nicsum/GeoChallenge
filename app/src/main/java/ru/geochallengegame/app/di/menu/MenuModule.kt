package ru.geochallengegame.app.di.menu

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.Module
import dagger.Provides
import ru.geochallengegame.app.ui.menu.MenuActivity
import ru.geochallengegame.app.ui.menu.OnClickMapListener
import ru.geochallengegame.app.ui.menu.vm.MenuMapsViewModel
import ru.geochallengegame.app.ui.menu.vm.MenuMapsViewModelFactory


@Module
class MenuModule {

    @Provides
    fun provideMenuMapsViewMode(
        activity: MenuActivity,
        factory: MenuMapsViewModelFactory
    ): MenuMapsViewModel {
        return ViewModelProvider(activity, factory).get(MenuMapsViewModel::class.java)
    }

    @Provides
    fun provideLinearLayoutManager(activity: MenuActivity): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    @Provides
    fun provideListener(activity: MenuActivity): OnClickMapListener {
        return activity
    }
}