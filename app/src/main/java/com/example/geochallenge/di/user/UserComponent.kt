package com.example.geochallenge.di.user


import com.example.geochallenge.di.game.GameComponent
import com.example.geochallenge.di.menu.MenuComponent
import com.google.firebase.auth.FirebaseUser
import dagger.BindsInstance
import dagger.Subcomponent

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