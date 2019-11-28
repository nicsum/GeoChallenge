package com.example.geochallenge.ui.game.street

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment

class StreetGameInfoFragment : BaseGameInfoFragment() {

    lateinit var nextCityButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        nextCityButton = v.findViewById(R.id.nextCityBtn)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as StreetGameActivity)
            .streetComponent
            .inject(this)
        nextCityButton.setOnClickListener { viewModel.nextTask() }

        super.onActivityCreated(savedInstanceState)
    }
}