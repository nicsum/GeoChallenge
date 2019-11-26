package com.example.geochallenge.ui.game.multiplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.GameActivity

class MultiplayerGameInfoFragment : BaseGameInfoFragment() {

    lateinit var waitingProgressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        waitingProgressBar = v.findViewById(R.id.waitingProgressBar)
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as GameActivity)
            .multiplayerComponent
            .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val vm = viewModel as MultiplayerViewModel

        vm.waitingPlayers.observe(this,
            Observer {
                if (it) {
                    waitingProgressBar.visibility = View.VISIBLE
                    gameInfo.visibility = View.GONE
                } else {
                    waitingProgressBar.visibility = View.GONE
                    gameInfo.visibility = View.VISIBLE
                }
            })
    }
}