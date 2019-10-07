package com.example.geochallenge.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.geochallenge.R

class MenuFragment : Fragment() {


    lateinit var startGameButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?, savedInstanceState: Bundle?  ): View? {
        val view = inflater.inflate(R.layout.fr_menu, container, false)
        startGameButton = view.findViewById(R.id.start_game_btn)
        startGameButton.setOnClickListener { startGame()}

        return view
    }

    fun startGame(){
        Toast.makeText(context, "start game", Toast.LENGTH_SHORT).show()
    }


}