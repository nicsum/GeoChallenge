package com.example.geochallenge.ui.game.timelimit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.GameActivity

class TimeLimitGameInfoFragment : BaseGameInfoFragment() {

    lateinit var timerTv: TextView
    lateinit var nextCityButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        timerTv = v.findViewById(R.id.timerTv)
        nextCityButton = v.findViewById(R.id.nextCityBtn)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as GameActivity)
            .timeComponent
            .inject(this)
        val vm = viewModel as TimeLimitGameViewModel

        vm.stillHaveTime.observe(this,
            Observer {
                timerTv.text = if (it == null) "" else
                    getString(
                        R.string.timer_info,
                        it
                    )
            })

        nextCityButton.setOnClickListener { viewModel.nextTask() }
        super.onActivityCreated(savedInstanceState)
    }

}