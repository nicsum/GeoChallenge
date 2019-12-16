package com.example.geochallenge.ui.game.classic

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.example.geochallenge.ui.game.FillProgressLayout
import javax.inject.Inject


class ClassicGameInfoFragment @Inject constructor() : BaseGameInfoFragment() {

    lateinit var nextCityButton: Button
    lateinit var pointsTv: TextView
    lateinit var timerTv: TextView
    lateinit var progressBar: FillProgressLayout

    //    @Inject
    lateinit var viewModel: ClassicGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        nextCityButton = v.findViewById(R.id.nextCityBtn)
        pointsTv = v.findViewById(R.id.pointsText)
        timerTv = v.findViewById(R.id.timerTv)
        progressBar = v.findViewById(R.id.progressBar)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel = (activity as ClassicGameActivity).viewModel
        viewModel.isTaskCompleted.observe(
            this,
            Observer { completed ->
                nextCityButton.visibility = if (completed) View.VISIBLE else View.GONE
            }

        )

        viewModel.points.observe(
            this,
            Observer {
            addPoints(it ?: 0)
        })

        viewModel.secondsPassed.observe(
            this,
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
        scoreAnimator.setDuration(3000)
        scoreAnimator.addUpdateListener { animation ->
            pointsTv.setText(animation.getAnimatedValue().toString())
        }
        scoreAnimator.start()
    }

}