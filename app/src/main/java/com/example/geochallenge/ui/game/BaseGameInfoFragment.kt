package com.example.geochallenge.ui.game

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import javax.inject.Inject

abstract class BaseGameInfoFragment : Fragment() {


    lateinit var distance: TextView
    lateinit var cityNameTv: TextView
    lateinit var taskCounterTv: TextView
    lateinit var currentLevelTv: TextView
    lateinit var gameInfo: RelativeLayout


    @Inject
    lateinit var viewModel: BaseGameViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distance = view.findViewById(R.id.distanceTv)
        cityNameTv = view.findViewById(R.id.cityNameText)
        taskCounterTv = view.findViewById(R.id.taskCounterText)
        currentLevelTv = view.findViewById(R.id.currentLevelText)
        gameInfo = view.findViewById(R.id.gameInfoCard)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.distance.observe(this,
            Observer {
                distance.text = if (it == null) "" else getString(R.string.distance_info, it)
            })
        viewModel.currentTask.observe(this,
            Observer { cityNameTv.text = if (it == null) "" else "${it.countryRU}, ${it.city}" })

        viewModel.taskCounter.observe(this,
            Observer { taskCounterTv.text = getString((R.string.location_d_text), it) })
        viewModel.currentLevel.observe(this,
            Observer { currentLevelTv.text = getString((R.string.level_d_text), it) })


    }

}