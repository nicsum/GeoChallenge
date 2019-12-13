package com.example.geochallenge.di.app


import com.example.geochallenge.AppDelegate
import com.example.geochallenge.di.user.UserComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, AppSubcomponents::class))
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appDelegate: AppDelegate): AppComponent
    }

    fun userComponent(): UserComponent.Factory

}