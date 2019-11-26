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
import com.example.geochallenge.ui.game.FillProgressLayout
import com.example.geochallenge.ui.game.GameActivity


class ClassicGameInfoFragment : BaseGameInfoFragment() {

    lateinit var nextCityButton: Button
    lateinit var progressBar: FillProgressLayout
    lateinit var timerTv: TextView
    lateinit var pointsTv: TextView
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        pointsTv = v.findViewById(R.id.pointsText)
        timerTv = v.findViewById(R.id.timerTv)
        progressBar = v.findViewById(R.id.progressBar)
        nextCityButton = v.findViewById(R.id.nextCityBtn)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        (activity as GameActivity)
            .classicComponent
            .inject(this)

        val vm = viewModel as ClassicGameViewModel

        vm.isTaskCompleted.observe(this,
            Observer { nextCityButton.visibility = if (it) View.VISIBLE else View.GONE })

        vm.secondsPassed.observe(this,
            Observer {
                progressBar.setProgress(it.toInt() * 100 / (13000 / 1000))
                timerTv.text = if (it == null) "" else
                    getString(
                        R.string.timer_info,
                        ClassicGameViewModel.SECONDS_FOR_TASK - it
                    )
            })

        vm.points.observe(this, Observer {
            addPoints(it ?: 0)
        })
        nextCityButton.setOnClickListener { viewModel.nextTask() }
        super.onActivityCreated(savedInstanceState)
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