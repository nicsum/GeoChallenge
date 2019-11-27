package com.example.geochallenge.di.app

import com.example.geochallenge.di.activity.ActivityComponent
import com.example.geochallenge.di.activity.GameActivityModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {

    fun provideActivityComponent(moduleGame: GameActivityModule): ActivityComponent

}