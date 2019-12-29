package com.example.geochallenge.ui.game

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.geochallenge.R


abstract class BaseGameInfoFragment : Fragment() {

    lateinit var distance: TextView
    lateinit var cityNameTv: TextView
    lateinit var taskCounterTv: TextView
    lateinit var currentLevelTv: TextView
    lateinit var gameInfoView: RelativeLayout

    lateinit var errorView: View
    lateinit var errorMessage: TextView
    lateinit var updateBtn: ImageButton

    lateinit var loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distance = view.findViewById(R.id.distanceTv)
        cityNameTv = view.findViewById(R.id.cityNameText)
        taskCounterTv = view.findViewById(R.id.taskCounterText)
        currentLevelTv = view.findViewById(R.id.currentLevelText)
        gameInfoView = view.findViewById(R.id.gameInfoCard)
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
            this,
            Observer {
                distance.text = if (it == null) "" else getString(R.string.distance_info, it)
            })
        vm.currentTask.observe(
            this,
            Observer { cityNameTv.text = if (it == null) "" else "${it.country}, ${it.name}" })

        vm.taskCounter.observe(
            this,
            Observer { taskCounterTv.text = getString((R.string.location_d_text), it) })
        vm.currentLevel.observe(
            this,
            Observer { currentLevelTv.text = getString((R.string.level_d_text), it) })

        vm.isLoadingVisible.observe(
            this,
            Observer {
                if (it) loadingView.visibility = View.VISIBLE
                else loadingView.visibility = View.GONE
            }
        )

        vm.isErrorVisible.observe(
            this,
            Observer {
                if (it) errorView.visibility = View.VISIBLE
                else errorView.visibility = View.GONE
            }
        )
        vm.error.observe(
            this,
            Observer {
                when (it) {
                    GameError.CONNECTION_ERROR -> errorMessage.text =
                        resources.getString(R.string.game_error_connection)
                    GameError.SERVER_ERROR -> errorMessage.text =
                        resources.getString(R.string.game_error_server)
                    GameError.ANY -> errorMessage.text =
                        resources.getString(R.string.game_error_any)
                }
            }
        )

        vm.isGameInfoVisible.observe(
            this,
            Observer {
                if (it) gameInfoView.visibility = View.VISIBLE
                else gameInfoView.visibility = View.GONE
            }
        )

    }

    abstract fun getViewModel(): BaseGameViewModel

}