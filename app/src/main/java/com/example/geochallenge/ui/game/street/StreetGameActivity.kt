package com.example.geochallenge.ui.game.street

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.street.StreetGameComponent
import com.example.geochallenge.di.street.StreetGameModule
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.LatLng

class StreetGameActivity : BaseGameMapActivity() {

    lateinit var switchBtn: Button
    lateinit var mapView: FrameLayout

    lateinit var streetView: StreetViewPanoramaView

    val streetComponent: StreetGameComponent by lazy {
        val countTasks = getCountTasksForLevel()
        activityComponent.provideStreetGameComponent(StreetGameModule(countTasks))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        switchBtn = findViewById(R.id.switchBtn)
        mapView = findViewById(R.id.mapScreen)
        streetView = findViewById(R.id.steet_view_panorama)

        switchBtn.setOnClickListener { switch() }
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()

        streetView.onCreate(savedInstanceState)
        streetView.getStreetViewPanoramaAsync { panorama ->
            getViewModel().currentTask.observe(this, Observer {
                panorama?.setPosition(LatLng(it.latitude!!, it.longitude!!))

            })
        }

    }

    override fun onStart() {
        super.onStart()
        streetView.onStart()
    }

    override fun onResume() {
        super.onResume()
        streetView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        streetView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        streetView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        streetView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        streetView.onDestroy()
    }

    override fun getViewModel(): BaseGameViewModel {
        return (supportFragmentManager
            .findFragmentById(R.id.game_info_container) as BaseGameInfoFragment).viewModel

    }


    private fun switch() {
        if (mapView.isVisible) {
            showStreet()
        } else {
            showMap()
        }
    }

    private fun showMap() {
        mapView.visibility = View.VISIBLE
        streetView.visibility = View.GONE
    }

    private fun showStreet() {
        mapView.visibility = View.GONE
        streetView.visibility = View.VISIBLE
    }

    override fun getLayout(): Int {
        return R.layout.ac_street
    }

    override fun getMapComponent(): MapComponent {
        return streetComponent
    }


}