package com.example.geochallenge.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.geochallenge.R

class GameInfoFragment : Fragment() {

    lateinit var distance: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        distance = v.findViewById(R.id.distance)
        return v
    }

    open fun setDistance(d: Int){
        distance.setText("$d км")
    }


}