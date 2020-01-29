package com.example.geochallenge.ui.records

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.records.RecordsComponent
import javax.inject.Inject


class RecordsActivity : AppCompatActivity(){

    @Inject
    lateinit var fragment: RecordsFragment


    private lateinit var recordsComponent: RecordsComponent


    override fun onCreate(savedInstanceState: Bundle?) {

        val gameComponent = (applicationContext as AppDelegate).gameComponent
        if (gameComponent == null) finish()
        else {
            recordsComponent = gameComponent.recordsComponent().create(this)
            recordsComponent.inject(this)
        }

        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        setContentView(R.layout.ac_records)
    }

    override fun onDestroy() {
        super.onDestroy()
        (applicationContext as AppDelegate).destroyGameComponent()
    }

}