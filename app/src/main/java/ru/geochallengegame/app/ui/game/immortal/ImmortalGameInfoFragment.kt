package ru.geochallengegame.app.ui.game.immortal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import ru.geochallengegame.R
import ru.geochallengegame.app.ui.game.BaseGameInfoFragment
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import javax.inject.Inject

class ImmortalGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {


    lateinit var viewModel: ImmortalGameViewModel

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        v.findViewById<TextView>(R.id.pointsText).visibility = View.GONE
        v.findViewById<TextView>(R.id.of).visibility = View.GONE
        v.findViewById<TextView>(R.id.ptsNextLvl).visibility = View.GONE
        v.findViewById<TextView>(R.id.pts).visibility = View.GONE
        return inflater.inflate(R.layout.fr_gameinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel = (activity as ImmortalGameActivity).viewModel

        viewModel.isTaskCompleted.observe(
            viewLifecycleOwner,
            Observer {
                nextCityButton.visibility = if (it) View.VISIBLE else View.GONE
            }
        )

        nextCityButton.setOnClickListener { viewModel.nextTask() }

        super.onActivityCreated(savedInstanceState)
    }
}