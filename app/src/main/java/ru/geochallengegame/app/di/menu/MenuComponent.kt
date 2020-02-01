package ru.geochallengegame.app.di.menu

import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.ui.menu.BaseMapsFragment
import ru.geochallengegame.app.ui.menu.MenuActivity


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