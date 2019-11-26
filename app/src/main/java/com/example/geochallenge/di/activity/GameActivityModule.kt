package com.example.geochallenge.di.activity

import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.game.classic.ClassicGameInfoFragment
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameInfoFragment
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameInfoFragment
import com.google.android.gms.maps.model.LatLng
import dagger.Module
import dagger.Provides


@Module
class GameActivityModule(
    val activity: GameActivity,
    val gameType: String,
    val startLocation: LatLng
) {

    @Provides
    @ActivityScope
    fun provideActivity(): GameActivity {
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
        return when (gameType) {
            GameActivity.CLASSIC_TYPE_GAME -> ClassicGameInfoFragment()
            GameActivity.TIME_LIMIT_TYPE_GAME -> TimeLimitGameInfoFragment()
            GameActivity.MULTIPLAYER_TYPE_GAME -> MultiplayerGameInfoFragment()
            else -> ClassicGameInfoFragment()
        }
    }


}