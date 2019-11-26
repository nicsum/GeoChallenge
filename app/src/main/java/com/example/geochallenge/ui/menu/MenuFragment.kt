package com.example.geochallenge.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.GameActivity
import com.example.geochallenge.ui.records.RecordsActivity

class MenuFragment : Fragment() {

    lateinit var startGameButton: Button
    lateinit var startLimitTimeGameButton: Button
    lateinit var showRecordsButton: Button
    lateinit var startMultiplayrButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu, container, false)
        startGameButton = view.findViewById(R.id.start_classic_game_btn)
        startLimitTimeGameButton = view.findViewById(R.id.time_trial_btn)
        startGameButton.setOnClickListener { startGame(GameActivity.CLASSIC_TYPE_GAME) }

        startMultiplayrButton = view.findViewById(R.id.multiplayer_btn)
        startMultiplayrButton.setOnClickListener { startGame(GameActivity.MULTIPLAYER_TYPE_GAME) }

        startLimitTimeGameButton.setOnClickListener{startGame(GameActivity.TIME_LIMIT_TYPE_GAME)}

        showRecordsButton = view.findViewById(R.id.records_btn)
        showRecordsButton.setOnClickListener{showTableOfRecords() }

        return view
    }


    private fun startGame(type : String){
        val intent = Intent(context, GameActivity::class.java)
        intent.putExtra(GameActivity.TYPE_GAME_KEY, type)
        intent.putExtra(GameActivity.START_LOCATION_KEY, Pair(64.0, 80.0))
        intent.putExtra(GameActivity.COUNT_TASKS_FOR_LEVEL_KEY, 5)

        activity?.startActivity(intent)
    }


    fun showTableOfRecords(){
        val intent = Intent(context, RecordsActivity::class.java)
        activity?.startActivity(intent)
    }

}