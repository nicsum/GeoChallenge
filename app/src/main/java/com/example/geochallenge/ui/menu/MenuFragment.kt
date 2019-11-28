package com.example.geochallenge.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameActivity
import com.example.geochallenge.ui.game.street.StreetGameActivity
import com.example.geochallenge.ui.game.timelimit.TimeLimitGameActivity
import com.example.geochallenge.ui.records.RecordsActivity

class MenuFragment : Fragment() {

    lateinit var startGameButton: Button
    lateinit var startLimitTimeGameButton: Button
    lateinit var showRecordsButton: Button
    lateinit var startMultiplayrButton: Button
    lateinit var settingsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu, container, false)
        startGameButton = view.findViewById(R.id.start_classic_game_btn)
        startLimitTimeGameButton = view.findViewById(R.id.time_trial_btn)
        startGameButton.setOnClickListener { startClassicGame() }

        startMultiplayrButton = view.findViewById(R.id.multiplayer_btn)
        startMultiplayrButton.setOnClickListener { startMultiplayerGame() }

        startLimitTimeGameButton.setOnClickListener { startTimeGame() }

        showRecordsButton = view.findViewById(R.id.records_btn)
        showRecordsButton.setOnClickListener{showTableOfRecords() }

        settingsButton = view.findViewById(R.id.settings_btn)
        settingsButton.setOnClickListener { startStreetGame() }

        return view
    }


    private fun startClassicGame() {
        val intent = Intent(context, ClassicGameActivity::class.java)
        intent.putExtra(BaseGameMapActivity.START_LOCATION_KEY, Pair(64.0, 80.0))
        intent.putExtra(BaseGameMapActivity.COUNT_TASKS_FOR_LEVEL_KEY, 5)
        activity?.startActivity(intent)
    }

    private fun startTimeGame() {
        val intent = Intent(context, TimeLimitGameActivity::class.java)
        intent.putExtra(BaseGameMapActivity.START_LOCATION_KEY, Pair(64.0, 80.0))
        intent.putExtra(BaseGameMapActivity.COUNT_TASKS_FOR_LEVEL_KEY, 5)
        activity?.startActivity(intent)
    }

    private fun startMultiplayerGame() {
        val intent = Intent(context, MultiplayerGameActivity::class.java)
        intent.putExtra(BaseGameMapActivity.START_LOCATION_KEY, Pair(64.0, 80.0))
        intent.putExtra(BaseGameMapActivity.COUNT_TASKS_FOR_LEVEL_KEY, 5)
        activity?.startActivity(intent)
    }

    private fun startStreetGame() {
        val intent = Intent(context, StreetGameActivity::class.java)
        intent.putExtra(BaseGameMapActivity.START_LOCATION_KEY, Pair(64.0, 80.0))
        intent.putExtra(BaseGameMapActivity.COUNT_TASKS_FOR_LEVEL_KEY, 5)
        activity?.startActivity(intent)
    }


    fun showTableOfRecords(){
        val intent = Intent(context, RecordsActivity::class.java)
        activity?.startActivity(intent)
    }

}