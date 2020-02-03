package ru.geochallengegame.app.di.activity

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import ru.geochallengegame.app.ui.game.BaseGameMapActivity
import ru.geochallengegame.app.ui.game.classic.ClassicGameViewModel
import ru.geochallengegame.app.ui.game.classic.ClassicGameViewModelFactory
import ru.geochallengegame.app.ui.game.endless.EndlessGameViewModel
import ru.geochallengegame.app.ui.game.endless.EndlessGameViewModelFactory
import ru.geochallengegame.app.ui.game.time.TimeLimitGameViewModel
import ru.geochallengegame.app.ui.game.time.TimeLimitGameViewModelFactory


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

    @Provides
    fun provideEndlessGameViewModule(
        activity: BaseGameMapActivity,
        factory: EndlessGameViewModelFactory
    ): EndlessGameViewModel {
        return ViewModelProvider(activity, factory).get(EndlessGameViewModel::class.java)
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