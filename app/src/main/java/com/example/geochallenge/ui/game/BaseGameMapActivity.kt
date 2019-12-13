package com.example.geochallenge.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.gms.maps.model.LatLng


abstract class BaseGameMapActivity : AppCompatActivity() {


    var isFirstStartActivity: Boolean = false

    var activityComponent: GameActivityComponent? = null

    abstract fun getLayout(): Int
    abstract fun getViewModel(): BaseGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        isFirstStartActivity = savedInstanceState == null

        setContentView(getLayout())
    }


    override fun onStart() {
        super.onStart()
        getViewModel().isGameFinished.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Игра окончена", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstStartActivity)
            getViewModel().newGame()
    }

    override fun onPause() {
        super.onPause()
        isFirstStartActivity = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        getViewModel().finishGame()
    }

    fun getMapId() = 1

    fun getGameInfo(mode: String, mapId: Int) = GameInfo(mode, mapId, 5)

    fun getStartLocation() = LatLng(64.0, 80.0)

}