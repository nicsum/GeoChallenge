package ru.geochallengegame.app.ui.game.classic

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fr_gameinfo.*
import ru.geochallengegame.R
import ru.geochallengegame.app.ui.game.BaseGameInfoFragment
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import ru.geochallengegame.app.ui.game.FillProgressLayout
import javax.inject.Inject


class ClassicGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {
    private lateinit var pointsTv: TextView
    private lateinit var timerTv: TextView
    private lateinit var progressBar: FillProgressLayout

    //    @Inject
    lateinit var viewModel: ClassicGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        pointsTv = v.findViewById(R.id.pointsText)
        timerTv = v.findViewById(R.id.timerTv)
        progressBar = v.findViewById(R.id.progressBar)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel = (activity as ClassicGameActivity).viewModel
        viewModel.isTaskCompleted.observe(
            viewLifecycleOwner,
            Observer {
                nextCityButton.visibility = if (it) View.VISIBLE else View.GONE
            }
        )
        viewModel.neededPoints.observe(
            viewLifecycleOwner,
            Observer {
                ptsNextLvl.text = it.toString()
            }
        )

        viewModel.points.observe(
            viewLifecycleOwner,
            Observer {
                addPoints(it ?: 0)
        })

        viewModel.secondsPassed.observe(
            viewLifecycleOwner,
            Observer {
                progressBar.setProgress(it.toInt() * 100 / (13000 / 1000))
                timerTv.text = if (it == null) "" else
                    getString(
                        R.string.timer_info,
                        ClassicGameViewModel.SECONDS_FOR_TASK - it
                    )
            })

        nextCityButton.setOnClickListener { viewModel.nextTask() }

        super.onActivityCreated(savedInstanceState)
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

    private fun addPoints(value: Int) {

        val oldValue = try {
            pointsTv.text.toString().toInt()
        } catch (e: Exception) {
            0
        }
        val scoreAnimator = ValueAnimator.ofInt(oldValue, value)
        scoreAnimator.animatedValue
        scoreAnimator.duration = 3000
        scoreAnimator.addUpdateListener { animation ->
            pointsTv.text = animation.animatedValue.toString()
        }
        scoreAnimator.start()
    }

}