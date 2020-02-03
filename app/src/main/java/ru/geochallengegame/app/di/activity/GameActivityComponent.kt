package ru.geochallengegame.app.di.activity


import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.ui.game.BaseGameMapActivity
import ru.geochallengegame.app.ui.game.GameMapFragment
import ru.geochallengegame.app.ui.game.classic.ClassicGameActivity
import ru.geochallengegame.app.ui.game.endless.EndlessGameActivity
import ru.geochallengegame.app.ui.game.time.TimeLimitGameActivity


@GameActivityScope
@Subcomponent(
    modules = [
        GameActivityModule::class
    ]
)
interface GameActivityComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance activity: BaseGameMapActivity
        ): GameActivityComponent


    }


    fun inject(activity: ClassicGameActivity)
    fun inject(activity: TimeLimitGameActivity)
    fun inject(activity: EndlessGameActivity)
    fun inject(fragment: GameMapFragment)
}