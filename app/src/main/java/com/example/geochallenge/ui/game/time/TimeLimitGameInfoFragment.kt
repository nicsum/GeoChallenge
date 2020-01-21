package com.example.geochallenge.ui.game.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameViewModel
import kotlinx.android.synthetic.main.fr_gameinfo.*
import javax.inject.Inject

class TimeLimitGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {

    lateinit var timerTv: TextView
//    lateinit var nextCityButton: Button

    //    @Inject
    lateinit var viewModel: TimeLimitGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        timerTv = v.findViewById(R.id.timerTv)
//        nextCityButton = v.findViewById(R.id.nextCityBtn)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {


        viewModel = (activity as TimeLimitGameActivity).viewModel

        viewModel.timer.observe(
            this,
            Observer {
                progressBar.setProgress(it.first.toInt() * 100 / it.second.toInt())
                pointsText.text = it.first.toString()
                ptsNextLvl.text = it.second.toString()
            })

//        nextCityButton.setOnClickListener { viewModel.nextTask() }
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

}