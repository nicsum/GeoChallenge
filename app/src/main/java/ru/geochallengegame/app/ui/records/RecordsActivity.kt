package ru.geochallengegame.app.ui.records

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.di.records.RecordsComponent


class RecordsActivity: AppCompatActivity() {

    lateinit var recordsComponent: RecordsComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        val gameComponent = (applicationContext as AppDelegate).gameComponent
        if (gameComponent == null) finish()
        else {
            recordsComponent = gameComponent.recordsComponent().create(this)
            recordsComponent.inject(this)
        }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.ac_records)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, RecordsFragment())
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        (applicationContext as AppDelegate).destroyGameComponent()
    }
}