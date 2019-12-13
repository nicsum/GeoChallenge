package com.example.geochallenge.di.records

import com.example.geochallenge.ui.records.RecordsActivity
import dagger.BindsInstance
import dagger.Subcomponent


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