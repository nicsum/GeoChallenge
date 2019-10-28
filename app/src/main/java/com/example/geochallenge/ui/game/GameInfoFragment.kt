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
import kotlin.reflect.KClass

class GameInfoFragment : Fragment() {

    lateinit var distanceTv: TextView
    lateinit var cityNameTv: TextView
    lateinit var stillHaveTimeTv: TextView
    lateinit var stillHaveDistanceTv: TextView
    lateinit var taskCounterTv: TextView
    lateinit var nextCityButton: Button

    lateinit var viewModelClass :  KClass<out SimpleGameViewModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_gameinfo, container, false)
        distanceTv = v.findViewById(R.id.distanceText)
        cityNameTv = v.findViewById(R.id.cityNameText)
        stillHaveTimeTv = v.findViewById(R.id.stillHaveTimeText)
        stillHaveDistanceTv = v.findViewById(R.id.stillHaveDistanceText)
        taskCounterTv = v.findViewById(R.id.taskCounterText)
        nextCityButton = v.findViewById(R.id.nextCityBtn)

        val viewModel =  ViewModelProviders.of(context as GameActivity).get(viewModelClass.java)

        nextCityButton.setOnClickListener{ viewModel.nextTask() }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val typeGame = activity?.intent?.getStringExtra(GameActivity.TYPE_GAME_KEY) ?: GameActivity.DEFAULT_TYPE_GAME
        viewModelClass = when(typeGame){
            GameActivity.DEFAULT_TYPE_GAME -> SimpleGameViewModel::class
            GameActivity.DISTANCE_LIMIT_TYPE_GAME -> DistanceLimitGameViewModel::class
            GameActivity.TIME_LIMIT_TYPE_GAME -> TimeLimitGameViewModel::class
            else -> SimpleGameViewModel::class
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel =  ViewModelProviders.of(context as GameActivity).get(viewModelClass.java)

            viewModel.distance.observe(this,
                Observer { distanceTv.text = if(it == null) "" else "$it км." })
            viewModel.currentTask.observe(this,
                Observer { cityNameTv.text = if(it== null)"" else "Найдите город ${it.name}" })
            viewModel.isTaskCompleted.observe(this,
                Observer { nextCityButton.visibility = if(it) View.VISIBLE else View.GONE })
            viewModel.taskCounter.observe(this,
                Observer { taskCounterTv.text = "Кол-во городов: $it" })

        if (viewModel is DistanceLimitGameViewModel){
           viewModel.stillHaveDistance.observe(this,
               Observer { stillHaveDistanceTv.text = "У вас осталось $it км"})

        }

        if(viewModel is TimeLimitGameViewModel){
            viewModel.stillHaveTime.observe(this,
                Observer { stillHaveTimeTv.text = "У вас осталось $it сек." }
                )
        }

    }
}