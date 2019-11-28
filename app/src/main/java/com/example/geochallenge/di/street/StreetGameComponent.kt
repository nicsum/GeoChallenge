package com.example.geochallenge.di.street

import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.ui.game.street.StreetGameInfoFragment
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(StreetGameModule::class))
@StreetGameScope
interface StreetGameComponent : MapComponent {

    fun inject(fragment: StreetGameInfoFragment)

}