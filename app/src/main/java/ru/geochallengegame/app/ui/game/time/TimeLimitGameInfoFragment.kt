package ru.geochallengegame.app.ui.game.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fr_gameinfo.*
import ru.geochallengegame.R
import ru.geochallengegame.app.ui.game.BaseGameInfoFragment
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import javax.inject.Inject

class TimeLimitGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {

    lateinit var viewModel: TimeLimitGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fr_gameinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ptsNextLvl.visibility = View.GONE
        of.visibility = View.GONE
        timerTv.visibility = View.VISIBLE
        nextCityBtn.visibility = View.GONE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel = (activity as TimeLimitGameActivity).viewModel

        viewModel.timer.observe(
            viewLifecycleOwner,
            Observer {
                val time = it.second - it.first
                if (time >= 0) {
                    progressBar.setProgress(it.first.toInt() * 100 / it.second.toInt())
                    timerTv.text = getString(R.string.timer_info, (time))
                }
            })

        viewModel.taskCounterGame.observe(
            viewLifecycleOwner,
            Observer {
                pointsText.text = if (it <= 0) "0" else "${it - 1}"
            }
        )

        viewModel.isGameFinished.observe(
            viewLifecycleOwner,
            Observer {
                if (it) timerTv.text = getString(R.string.timer_info, 0)
            })

//        nextCityButton.setOnClickListener { viewModel.nextTask() }
        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

}