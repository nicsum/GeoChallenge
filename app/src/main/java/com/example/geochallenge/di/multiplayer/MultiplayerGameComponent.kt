package com.example.geochallenge.di.multiplayer

import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameInfoFragment
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(MultiplayerGameModule::class, MultiplayerApiModule::class))
@MultiplayerGameScope
interface MultiplayerGameComponent : MapComponent {
    fun inject(fragment: MultiplayerGameInfoFragment)
}