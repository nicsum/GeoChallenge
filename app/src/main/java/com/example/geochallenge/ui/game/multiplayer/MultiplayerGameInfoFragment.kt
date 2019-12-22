package com.example.geochallenge.ui.game.multiplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameViewModel
import javax.inject.Inject

class MultiplayerGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {


    //    @Inject
    lateinit var viewModel: MultiplayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = (activity as MultiplayerGameActivity).viewModel

    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }
}