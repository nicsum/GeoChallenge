package com.example.geochallenge.di.menu

import com.example.geochallenge.ui.menu.BaseMapsFragment
import com.example.geochallenge.ui.menu.MenuActivity
import dagger.BindsInstance
import dagger.Subcomponent


@MenuScope
@Subcomponent(
    modules = [
        MenuModule::class
    ]
)
interface MenuComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: MenuActivity): MenuComponent
    }

    fun inject(activity: MenuActivity)

    fun inject(fragment: BaseMapsFragment)
}