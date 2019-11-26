package com.example.geochallenge.di.time


import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameInfoFragment
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(TimeGameModule::class))
@TimeGameScope
interface TimeGameComponent : MapComponent {
    fun inject(fragment: TimeLimitGameInfoFragment)
//    fun inject(fragment : GameMapFragment)
}