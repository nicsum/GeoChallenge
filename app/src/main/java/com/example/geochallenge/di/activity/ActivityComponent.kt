package com.example.geochallenge.di.activity

import com.example.geochallenge.di.classic.ClassicGameComponent
import com.example.geochallenge.di.classic.ClassicGameModule
import com.example.geochallenge.di.multiplayer.MultiplayerGameComponent
import com.example.geochallenge.di.multiplayer.MultiplayerGameModule
import com.example.geochallenge.di.time.TimeGameComponent
import com.example.geochallenge.di.time.TimeGameModule
import com.example.geochallenge.ui.game.GameActivity
import dagger.Subcomponent


@ActivityScope
@Subcomponent(modules = arrayOf(GameActivityModule::class))
interface ActivityComponent {
    fun inject(activity: GameActivity)

    fun provideClassicGameComponent(module: ClassicGameModule): ClassicGameComponent
    fun provideTimeGameComponent(module: TimeGameModule): TimeGameComponent
    fun provideMultiplayerGameComponent(module: MultiplayerGameModule): MultiplayerGameComponent

}