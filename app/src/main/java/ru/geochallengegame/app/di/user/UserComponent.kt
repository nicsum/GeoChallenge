package ru.geochallengegame.app.di.user


import com.google.firebase.auth.FirebaseUser
import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.di.game.GameComponent
import ru.geochallengegame.app.di.menu.MenuComponent

@LoggedUserScope
@Subcomponent(
    modules = [
        UserSubcomponents::class
    ]
)
interface UserComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance firebaseUser: FirebaseUser): UserComponent
    }


    fun gameComponent(): GameComponent.Factory

    fun menuComponent(): MenuComponent.Factory
}