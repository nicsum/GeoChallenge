package com.example.geochallenge.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.R
import com.example.geochallenge.ui.records.RecordsActivity
import kotlin.reflect.KClass


class GameActivity : AppCompatActivity() {

    companion object{

        const val TYPE_GAME_KEY = "TYPE_GAME_KEY"
        const val START_LOCATION_KEY = "START_LOCATION_KEY"

        const val DEFAULT_TYPE_GAME = "DEFAULT_TYPE_GAME"
        const val CLASSIC_TYPE_GAME = "CLASSIC_TYPE_GAME"
        const val TIME_LIMIT_TYPE_GAME = "TIME_LIMIT_TYPE_GAME"
        const val MULTIPLAYER_TYPE_GAME = "MULTIPLAYER_TYPE_GAME"
    }

    var isFirstStartActivity: Boolean = false

    lateinit var viewModelClass :  KClass<out SimpleGameViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_game)
        isFirstStartActivity = savedInstanceState == null

        val typeGame = intent.extras?.getString(TYPE_GAME_KEY) ?: DEFAULT_TYPE_GAME

        viewModelClass = when(typeGame){
            DEFAULT_TYPE_GAME -> SimpleGameViewModel::class
            CLASSIC_TYPE_GAME -> ClassicGameViewModel::class
            TIME_LIMIT_TYPE_GAME  -> TimeLimitGameViewModel::class
            MULTIPLAYER_TYPE_GAME  -> MultiplayerViewModel::class
            else -> SimpleGameViewModel::class
        }

        val viewModel = ViewModelProviders.of(this).get(viewModelClass.java)
        viewModel.isGameFinished.observe(this , Observer {
            if(it){
                Toast.makeText(this, "Игра окончена", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                finish()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if(isFirstStartActivity)
            ViewModelProviders.of(this).get(viewModelClass.java).newGame()
    }

    override fun onPause() {
        super.onPause()
        isFirstStartActivity = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewModelProviders.of(this).get(viewModelClass.java).finishGame()
    }


}