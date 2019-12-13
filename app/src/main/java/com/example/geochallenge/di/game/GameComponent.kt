package com.example.geochallenge.di.game


import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.GameMapFragment
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameActivity
import com.example.geochallenge.ui.game.street.StreetGameActivity
import com.example.geochallenge.ui.game.time.TimeLimitGameActivity
import com.google.android.gms.maps.model.LatLng
import dagger.BindsInstance
import dagger.Subcomponent


@GameScope
@Subcomponent(modules = arrayOf(GameModule::class))
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance gameInfo: GameInfo,
            @BindsInstance startLocation: LatLng,
            @BindsInstance activity: BaseGameMapActivity
        ): GameComponent
    }

    fun inject(activity: ClassicGameActivity)
    fun inject(activity: StreetGameActivity)
    fun inject(activity: TimeLimitGameActivity)
    fun inject(activity: MultiplayerGameActivity)
    fun inject(fragment: GameMapFragment)


//
//    fun provideClassicGameComponent(module: ClassicGameModule): ClassicGameComponent
//    fun provideTimeGameComponent(module: TimeGameModule): TimeGameComponent
//    fun provideMultiplayerGameComponent(module: MultiplayerGameModule): MultiplayerGameComponent
//    fun provideStreetGameComponent(module: StreetGameModule): StreetGameComponent
}