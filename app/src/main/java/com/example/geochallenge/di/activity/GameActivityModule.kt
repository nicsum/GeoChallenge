package com.example.geochallenge.di.activity

import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import com.example.geochallenge.ui.game.classic.ClassicGameViewModelFactory
import com.example.geochallenge.ui.game.time.TimeLimitGameViewModel
import com.example.geochallenge.ui.game.time.TimeLimitGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class GameActivityModule {

    @Provides
    fun provideClassicViewModel(
        activity: BaseGameMapActivity,
        factory: ClassicGameViewModelFactory
    ): ClassicGameViewModel {
        return ViewModelProvider(activity, factory).get(ClassicGameViewModel::class.java)
    }

    @Provides
    fun provideTimeLimitGameViewModule(
        activity: BaseGameMapActivity,
        factory: TimeLimitGameViewModelFactory
    ): TimeLimitGameViewModel {
        return ViewModelProvider(activity, factory).get(TimeLimitGameViewModel::class.java)
    }

//    @Provides
//    fun provideStreetViewModule(
//        activity: BaseGameMapActivity,
//        factory: StreetGameViewModelFactory
//    ): StreetGameViewModel {
//        return ViewModelProvider(activity, factory).get(StreetGameViewModel::class.java)
//    }
//
//    @Provides
//    fun provideMultiplayerViewMode(
//        activity: BaseGameMapActivity,
//        factory: MultiplayerViewModelFactory
//    ): MultiplayerViewModel {
//        return ViewModelProvider(activity, factory).get(MultiplayerViewModel::class.java)
//    }
//
//    @Provides
//    fun provideFirebaseApi(api: FirebaseMultiplayerApi): MultiplayerApi {
//        return api
//    }
}