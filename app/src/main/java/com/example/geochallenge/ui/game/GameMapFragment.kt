package com.example.geochallenge.ui.game

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.game.Task
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.reflect.KFunction0

class GameMapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    var map: GoogleMap? = null
    lateinit var viewModel: GameViewModel

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        retainInstance = true
        getMapAsync(this)
    }

    //api 23?
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelProviders.of(context as FragmentActivity).get(GameViewModel::class.java)
    }

    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)
        viewModel.currentTask.observe(this, Observer { clearMap() } )
        viewModel.clickedPositions.observe(this, Observer {it?.let { addMarks(it.first, it.second) }
        })
        viewModel.taskCompeted.observe(this, Observer {
            if(it) map?.setOnMapClickListener(null)
            else map?.setOnMapClickListener(this) })
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.setOnMapClickListener(this)
    }

    override fun onMapClick(position: LatLng?) {
        if(position!=null){
            viewModel.onClickPosition(position)
        }
    }
    fun clearMap(){
        map?.clear()
    }

    fun addMarks(position: LatLng?, distance: String?){
        map?.addMarker(position?.let { MarkerOptions().position(it).title(distance) })

    }

}

