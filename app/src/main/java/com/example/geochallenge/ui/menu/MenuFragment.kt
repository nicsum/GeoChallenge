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
    lateinit var showRecordsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu, container, false)
        startGameButton = view.findViewById(R.id.start_game_btn)
        startGameButton.setOnClickListener { startGame()}

        showRecordsButton = view.findViewById(R.id.records_btn)
        showRecordsButton.setOnClickListener{showTableOfRecords() }

        return view
    }

    fun startGame(){
        val intent = Intent(context, GameActivity::class.java)
        activity?.startActivity(intent)
    }

    fun showTableOfRecords(){
        val intent = Intent(context, RecordsActivity::class.java)
        activity?.startActivity(intent)
    }


}