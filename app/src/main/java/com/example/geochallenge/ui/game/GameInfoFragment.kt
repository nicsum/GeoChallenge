package com.example.geochallenge.ui.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.R

class GameInfoFragment : Fragment() {

    lateinit var viewModel: GameViewModel
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

        nextCityButton.setOnClickListener{viewModel.newTask()}
        return v
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =  ViewModelProviders.of(context as FragmentActivity).get(GameViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.distance.observe(this, Observer { distanceTv.text = if(it == null) "" else "$it км." })
        viewModel.currentTask.observe(this , Observer { cityNameTv.text = if(it== null)"" else "Найдите город ${it.name}" })
        viewModel.timerText.observe(this, Observer { timerTv.text = it })
        viewModel.currentTaskPoints.observe(this, Observer { currentPointsTv.text = "Счет за уровень: ${it ?: 0} очков" })
        viewModel.gamePoints.observe(this, Observer { gamePointsTv.text = "Итого: ${it?:0} очков" })
        viewModel.taskCompeted.observe(this, Observer { nextCityButton.visibility = if(it) View.VISIBLE else View.GONE })

        viewModel

    }


}