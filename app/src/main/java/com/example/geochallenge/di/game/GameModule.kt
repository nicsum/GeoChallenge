package com.example.geochallenge.di.game

import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.game.multiplayer.FirebaseMultiplayerApi
import com.example.geochallenge.game.multiplayer.MultiplayerApi
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.classic.ClassicGameViewModel
import com.example.geochallenge.ui.game.classic.ClassicGameViewModelFactory
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModel
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModelFactory
import com.example.geochallenge.ui.game.street.StreetGameViewModel
import com.example.geochallenge.ui.game.street.StreetGameViewModelFactory
import com.example.geochallenge.ui.game.time.TimeLimitGameViewModel
import com.example.geochallenge.ui.game.time.TimeLimitGameViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class GameModule {

    @Provides
    fun provideClassicViewModel(
        activity: BaseGameMapActivity,
        factory: ClassicGameViewModelFactory
    ): ClassicGameViewModel {
        return ViewModelProviders.of(activity, factory).get(ClassicGameViewModel::class.java)
    }

    @Provides
    fun provideTimeLimitGameViewModule(
        activity: BaseGameMapActivity,
        factory: TimeLimitGameViewModelFactory
    ): TimeLimitGameViewModel {
        return ViewModelProviders.of(activity, factory).get(TimeLimitGameViewModel::class.java)
    }

    @Provides
    fun provideStreetViewModule(
        activity: BaseGameMapActivity,
        factory: StreetGameViewModelFactory
    ): StreetGameViewModel {
        return ViewModelProviders.of(activity, factory).get(StreetGameViewModel::class.java)
    }

    @Provides
    fun provideMultiplayerViewMode(
        activity: BaseGameMapActivity,
        factory: MultiplayerViewModelFactory
    ): MultiplayerViewModel {
        return ViewModelProviders.of(activity, factory).get(MultiplayerViewModel::class.java)
    }


    @Provides
    fun provideFirebaseApi(api: FirebaseMultiplayerApi): MultiplayerApi {
        return api
    }
//    @Provides
//    fun provideGameInfoFragment (gameInfo: GameInfo): BaseGameInfoFragment {
//        return when (gameInfo.mode) {
//            "solo" -> ClassicGameInfoFragment()
//            "time" -> TimeLimitGameInfoFragment()
//            "mp" -> MultiplayerGameInfoFragment()
//            "street" -> StreetGameInfoFragment()
//            else -> ClassicGameInfoFragment()
//        }
//    }


}