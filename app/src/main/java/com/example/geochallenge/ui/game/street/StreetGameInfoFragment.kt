package com.example.geochallenge.ui.game.street

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameViewModel
import javax.inject.Inject

class StreetGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {

    lateinit var nextCityButton: CardView

    //    @Inject
    lateinit var viewModel: StreetGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        nextCityButton = v.findViewById(R.id.scoreTableLayout)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        viewModel = (activity as StreetGameActivity).viewModel
//        (activity as StreetGameActivity)
//            .streetComponent
//            .inject(this)
        nextCityButton.setOnClickListener { viewModel.nextTask() }

        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }
}