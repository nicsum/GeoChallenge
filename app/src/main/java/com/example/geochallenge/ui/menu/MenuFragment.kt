package com.example.geochallenge.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameActivity
import com.example.geochallenge.ui.game.street.StreetGameActivity
import com.example.geochallenge.ui.game.time.TimeLimitGameActivity
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.gms.maps.model.LatLng

class MenuFragment : Fragment() {

    lateinit var startGameButton: Button
    lateinit var startLimitTimeGameButton: Button
    lateinit var showRecordsButton: Button
    lateinit var startMultiplayrButton: Button
    lateinit var settingsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu_old, container, false)
        startGameButton = view.findViewById(R.id.start_classic_game_btn)
        startLimitTimeGameButton = view.findViewById(R.id.time_trial_btn)
        startGameButton.setOnClickListener {
            startClassicGame()
        }

        startMultiplayrButton = view.findViewById(R.id.multiplayer_btn)
        startMultiplayrButton.setOnClickListener {
            startMultiplayerGame()
        }

        startLimitTimeGameButton.setOnClickListener {
            startTimeGame()
        }

        showRecordsButton = view.findViewById(R.id.records_btn)
        showRecordsButton.setOnClickListener{showTableOfRecords() }

        settingsButton = view.findViewById(R.id.settings_btn)
        settingsButton.setOnClickListener {
            startStreetGame()
        }

        return view
    }

    private fun startClassicGame() {
        val intent = Intent(context, ClassicGameActivity::class.java)
        activity?.startActivity(intent)
        createGameComponent("solo")
    }

    private fun startTimeGame() {
        val intent = Intent(context, TimeLimitGameActivity::class.java)
        activity?.startActivity(intent)
        createGameComponent("solo")
    }

    private fun startMultiplayerGame() {
        val intent = Intent(context, MultiplayerGameActivity::class.java)
        activity?.startActivity(intent)
        createGameComponent("mp")
    }

    private fun startStreetGame() {
        val intent = Intent(context, StreetGameActivity::class.java)
        activity?.startActivity(intent)
        createGameComponent("street")
    }

    private fun createGameComponent(mode: String) {
        val gameInfo = getGameInfo(mode, getMapId())
        val startLocation = getStartLocation()
        (activity?.applicationContext as AppDelegate)
            .createGameComponent(gameInfo, startLocation)
    }

    fun showTableOfRecords(){
        val intent = Intent(context, RecordsActivity::class.java)
        activity?.startActivity(intent)
    }

    private fun getMapId() = 1
    private fun getGameInfo(mode: String, mapId: Int) = GameInfo(mode, mapId, 5, "ru")

    private fun getStartLocation() = LatLng(64.0, 80.0)

}