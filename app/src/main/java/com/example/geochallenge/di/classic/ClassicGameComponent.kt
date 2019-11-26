package com.example.geochallenge.di.classic

import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.ui.game.classic.ClassicGameInfoFragment
import dagger.Subcomponent


@Subcomponent(modules = arrayOf(ClassicGameModule::class))
@ClassicGameScope
interface ClassicGameComponent : MapComponent {

    fun inject(fragment: ClassicGameInfoFragment)
//    fun inject(fragment : GameMapFragment)
}