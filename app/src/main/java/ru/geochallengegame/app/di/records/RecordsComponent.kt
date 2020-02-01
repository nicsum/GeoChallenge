package ru.geochallengegame.app.di.records

import dagger.BindsInstance
import dagger.Subcomponent
import ru.geochallengegame.app.ui.records.RecordsActivity


@RecordsScope
@Subcomponent(
    modules = [
        RecordsModule::class
    ]
)
interface RecordsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: RecordsActivity): RecordsComponent
    }

    fun inject(activity: RecordsActivity)

}