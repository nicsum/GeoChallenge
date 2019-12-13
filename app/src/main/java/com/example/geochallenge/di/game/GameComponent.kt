package com.example.geochallenge.di.game


import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.di.records.RecordsComponent
import com.example.geochallenge.game.GameInfo
import com.google.android.gms.maps.model.LatLng
import dagger.BindsInstance
import dagger.Subcomponent


@GameScope
@Subcomponent(
    modules = [
        GameSubcomponents::class]
)
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance gameInfo: GameInfo,
            @BindsInstance startLocation: LatLng
        ): GameComponent
    }

    fun gameActivityComponent(): GameActivityComponent.Factory
    fun recordsComponent(): RecordsComponent.Factory

}