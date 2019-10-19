package com.example.geochallenge.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.R

class GameInfoFragment : Fragment() {

    var viewModel: GameViewModel? = null
    lateinit var distanceTv: TextView
    lateinit var cityNameTv: TextView
    lateinit var timerTv: TextView
    lateinit var currentPointsTv: TextView
    lateinit var gamePointsTv: TextView
    lateinit var nextCityButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        distanceTv = v.findViewById(R.id.distanceText)
        cityNameTv = v.findViewById(R.id.cityNameText)
        timerTv = v.findViewById(R.id.timerText)
        currentPointsTv = v.findViewById(R.id.currentPointsText)
        gamePointsTv = v.findViewById(R.id.gamePointsText)
        nextCityButton = v.findViewById(R.id.nextCityBtn)

        nextCityButton.setOnClickListener{viewModel?.newTask()}
        return v
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =  ViewModelProviders.of(context as GameActivity).get(GameViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        viewModel =  ViewModelProviders.of(context as GameActivity).get(GameViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()
        viewModel = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel?.let {
            it.distance.observe(this, Observer { distanceTv.text = if(it == null) "" else "$it км." })
            it.currentTask.observe(this , Observer { cityNameTv.text = if(it== null)"" else "Найдите город ${it.name}" })
            it.timerText.observe(this, Observer { timerTv.text = it })
            it.currentTaskPoints.observe(this, Observer { currentPointsTv.text = "Счет за уровень: ${it ?: 0} очков" })
            it.gamePoints.observe(this, Observer { gamePointsTv.text = "Итого: ${it?:0} очков" })
            it.taskCompeted.observe(this, Observer { nextCityButton.visibility = if(it) View.VISIBLE else View.GONE })
        }


    }


}