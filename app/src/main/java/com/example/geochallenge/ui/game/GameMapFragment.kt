package com.example.geochallenge.ui.game

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.game.Task
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import java.util.ArrayList


class GameMapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    var map: GoogleMap? = null
    var positionsMarkets : ArrayList<LatLng> = ArrayList()
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
            if(it) {
                map?.setOnMapClickListener(null)
                map?.setOnMarkerClickListener { false}
                viewModel.currentTask.value?.let{showAnswer(it)}
            }
            else {
                map?.setOnMapClickListener(this)
                map?.setOnMarkerClickListener { true}
                positionsMarkets.clear()
                showStartPosition()
            }  })
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
        map?.addMarker(position?.let {
            positionsMarkets.add(it)
            MarkerOptions().position(it).title(distance)
        })

    }
    private fun showStartPosition(){
        val defaultPosition = LatLng(0.0, 0.0)
        val location = CameraUpdateFactory.newLatLngZoom(defaultPosition, 1.0f) // вынести переменную
        map?.animateCamera(location)
    }

    private fun showAnswer(task: Task){
        val answerPosition = LatLng(task.latitude, task.longitude)
        val answerMarket = MarkerOptions()
            .position(answerPosition)
            .title(task.name)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        map?.let{
            it.addMarker(answerMarket)
            positionsMarkets.add(answerPosition)
            zoomMarkets(positionsMarkets)
        }
//        zoomMarkets(positionsMarkets)
//        val location = CameraUpdateFactory.newLatLngZoom(answerPosition, 3.0f) //вынести переменную


    }

    private fun zoomMarkets(positionsMarkets : List<LatLng>){

        if(positionsMarkets.size == 1){
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(positionsMarkets[0], 3.0f))
        }else{
            val builder = LatLngBounds.Builder()
            positionsMarkets.forEach{ builder.include(it) }
            val padding = 200
            val cu =  CameraUpdateFactory.newLatLngBounds(builder.build(), padding)
            map?.animateCamera(cu)
        }
    }

}

