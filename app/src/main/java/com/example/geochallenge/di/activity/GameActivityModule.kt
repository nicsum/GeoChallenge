package com.example.geochallenge.di.activity

import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.classic.ClassicGameInfoFragment
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameInfoFragment
import com.example.geochallenge.ui.game.street.StreetGameActivity
import com.example.geochallenge.ui.game.street.StreetGameInfoFragment
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameActivity
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameInfoFragment
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides


@Module
class GameActivityModule(
    val activity: BaseGameMapActivity,
    val startLocation: LatLng
) {

    @Provides
    @ActivityScope
    fun provideActivity(): BaseGameMapActivity {
        return activity
    }

    @Provides
    @ActivityScope
    fun provideStartLocation(): LatLng {
        return startLocation
    }

    @Provides
    @ActivityScope
    fun provideGameInfoFragmentFragment(): BaseGameInfoFragment {
        return when (activity) {
            is ClassicGameActivity -> ClassicGameInfoFragment()
            is TimeLimitGameActivity -> TimeLimitGameInfoFragment()
            is MultiplayerGameActivity -> MultiplayerGameInfoFragment()
            is StreetGameActivity -> StreetGameInfoFragment()
            else -> ClassicGameInfoFragment()
        }
    }


}