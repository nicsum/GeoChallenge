package com.example.geochallenge.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.records.RecordsActivity
import okhttp3.*
import java.io.IOException
import java.net.URI

class MenuFragment : Fragment() {

    lateinit var startGameButton: Button
    lateinit var startLimitTimeGameButton: Button
    lateinit var startLimitDistanceGameButton: Button
    lateinit var showRecordsButton: Button
    lateinit var multiplayerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu, container, false)
        startGameButton = view.findViewById(R.id.start_simple_game_btn)
        startLimitTimeGameButton = view.findViewById(R.id.start_limit_time_game_btn)
        startLimitDistanceGameButton = view.findViewById(R.id.start_limit_distance_game)
        multiplayerButton = view.findViewById(R.id.multiplayer_btn)


        startGameButton.setOnClickListener { startGame(GameActivity.DEFAULT_TYPE_GAME)}
        startLimitTimeGameButton.setOnClickListener{startGame(GameActivity.TIME_LIMIT_TYPE_GAME)}
        startLimitDistanceGameButton.setOnClickListener{startGame(GameActivity.DISTANCE_LIMIT_TYPE_GAME)}
        multiplayerButton.setOnClickListener{startGame(GameActivity.MULTIPLAYER_TYPE_GAME)}

        showRecordsButton = view.findViewById(R.id.records_btn)
        showRecordsButton.setOnClickListener{showTableOfRecords() }

        return view
    }

    private fun startGame(type : String){
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(GameActivity.TYPE_GAME_KEY, type)

        activity?.startActivity(intent)
    }


    fun showTableOfRecords(){
        val intent = Intent(context, RecordsActivity::class.java)
        activity?.startActivity(intent)
    }

}