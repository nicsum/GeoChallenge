package ru.geochallengegame.app.ui.game.classic

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


open class ClassicGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {
    //    @Inject
//    lateinit var viewModel: ClassicGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_gameinfo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val viewModel = getViewModel() as ClassicGameViewModel
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
                val curr: Double = it.toDouble()
                val max: Double = ((viewModel.currentLevel.value!!) *5000).toDouble()
                progressBarLevel.setProgress(((curr/max) *100).toInt())
            })


        viewModel.secondsPassed.observe(
            viewLifecycleOwner,
            Observer {
                progressBarTime.setProgress(it.toInt() * 100 / (13000 / 1000))
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
        return (activity as ClassicGameActivity).viewModel
    }

    private fun addPoints(value: Int) {
        val oldValue = try {
            pointsText.text.toString().toInt()
        } catch (e: Exception) {
            0
        }
        if (oldValue == value) return

    }

}