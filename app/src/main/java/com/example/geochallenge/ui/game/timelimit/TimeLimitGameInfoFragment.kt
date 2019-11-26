package com.example.geochallenge.ui.game.timelimit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.GameActivity

class TimeLimitGameInfoFragment : BaseGameInfoFragment() {

    lateinit var timerTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        timerTv = v.findViewById(R.id.timerTv)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as GameActivity)
            .timeComponent
            .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val vm = viewModel as TimeLimitGameViewModel

        vm.stillHaveTime.observe(this,
            Observer {
                timerTv.text = if (it == null) "" else
                    getString(
                        R.string.timer_info,
                        it
                    )
            })
    }

}