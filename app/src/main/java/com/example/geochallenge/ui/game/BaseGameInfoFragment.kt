package com.example.geochallenge.ui.game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import kotlinx.android.synthetic.main.fr_gameinfo.*


abstract class BaseGameInfoFragment : Fragment() {

    protected lateinit var nextCityButton: Button
    protected lateinit var distance: TextView
    private lateinit var cityNameTv: TextView
    private lateinit var taskCounterTv: TextView
    private lateinit var currentLevelTv: TextView
    private lateinit var gameInfoView: RelativeLayout
    private lateinit var errorView: View
    private lateinit var errorMessage: TextView
    private lateinit var updateBtn: ImageButton

    lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distance = view.findViewById(R.id.distanceTv)
        cityNameTv = view.findViewById(R.id.cityNameText)
        taskCounterTv = view.findViewById(R.id.taskCounterText)
        currentLevelTv = view.findViewById(R.id.currentLevelText)
        gameInfoView = view.findViewById(R.id.gameInfoCard)
        nextCityButton = view.findViewById(R.id.nextCityBtn)
        errorView = view.findViewById(R.id.error_view)
        errorMessage = errorView.findViewById(R.id.error_message)
        updateBtn = errorView.findViewById(R.id.update_btn)
        loadingView = view.findViewById(R.id.loading_view)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            cityNameTv,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val vm = getViewModel()

        updateBtn.setOnClickListener { vm.updateTasks() }
        vm.distance.observe(
            viewLifecycleOwner,
            Observer {
                distance.text = getDistanceText(it)
            })

        vm.currentTask.observe(
            viewLifecycleOwner,
            Observer { cityNameTv.text = if (it == null) "" else "${it.country}, ${it.name}" })

        vm.maxCountTasksForLevel.observe(
            viewLifecycleOwner,
            Observer {
                maxCountTasksText.text = it.toString()
            }
        )
        vm.taskCounterLevel.observe(
            viewLifecycleOwner,
            Observer { taskCounterTv.text = getString((R.string.task_counter_text), it) })

        vm.currentLevel.observe(
            viewLifecycleOwner,
            Observer { currentLevelTv.text = getString((R.string.level_d_text), it) })

        vm.isLoadingVisible.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    loadingView.visibility = View.VISIBLE
                    nextCityButton.visibility = View.GONE
                }
                else loadingView.visibility = View.GONE
            }
        )

        vm.isErrorVisible.observe(
            viewLifecycleOwner,
            Observer {
                if (it) {
                    errorView.visibility = View.VISIBLE
                    nextCityButton.visibility = View.GONE
                }
                else errorView.visibility = View.GONE
            }
        )
        vm.error.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    GameError.CONNECTION_ERROR -> errorMessage.text =
                        resources.getString(R.string.game_error_connection)
                    GameError.SERVER_ERROR -> errorMessage.text =
                        resources.getString(R.string.game_error_server)
                    else -> errorMessage.text =
                        resources.getString(R.string.game_error_any)
                }
            }
        )

        vm.isGameInfoVisible.observe(
            viewLifecycleOwner,
            Observer {

                if (it) gameInfoView.visibility = View.VISIBLE
                else gameInfoView.visibility = View.GONE
            }
        )

    }

    private fun getDistanceText(distance: Double?): String {
        if (distance == null) return ""
        return if (distance < 5) getString(
            R.string.distance_info,
            "${(distance * 1000).toInt()} м."
        )
        else getString(R.string.distance_info, "${distance.toInt()} км.")
    }

    abstract fun getViewModel(): BaseGameViewModel

}