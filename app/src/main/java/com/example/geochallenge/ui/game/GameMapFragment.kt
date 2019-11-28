package com.example.geochallenge.ui.game

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.geochallenge.game.CityTask
import com.example.geochallenge.ui.game.multiplayer.MultiplayerViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject


class GameMapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener  {

    var map: GoogleMap? = null

    @Inject
    lateinit var viewModel: BaseGameViewModel
    @Inject
    lateinit var startLocation: LatLng


    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        retainInstance = true
        getMapAsync(this)
    }


    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)

        (activity as BaseGameMapActivity).getMapComponent().inject(this)

        viewModel.isDefaultMapState.observe(this, Observer {
            if(it){
                map?.clear()
            }
        })
        viewModel.clickedPosition.observe(this,
            Observer {it?.let { addMarks(LatLng(it.first, it.second) , viewModel.distance.value) }
            })
        viewModel.isTaskCompleted.observe(this, Observer {
            if(it) {
                map?.setOnMapClickListener(null)
                map?.setOnMarkerClickListener { false}
                //TODO вынести формирование ответа во вьюмодел или придумай что-нибудь
                viewModel.currentTask.value?.let{ task ->
                    val clickedPosition = LatLng(
                        viewModel.clickedPosition.value?.first ?: 0.0,
                        viewModel.clickedPosition.value?.second ?: 0.0)

                    val playersAnswer = (viewModel as? MultiplayerViewModel)?.playersAnswer
                        ?.value
                        ?.values
                        ?.filterNotNull()
                        ?.map { coordinates -> LatLng(coordinates.first, coordinates.second) }

                    showAnswer(task, clickedPosition, playersAnswer)
                }
            }
            else {
                map?.setOnMapClickListener(this)
                map?.setOnMarkerClickListener { true}
//                showStartPosition()
            }  })

        viewModel.isDefaultMapState.observe(this, Observer {
            if(it)
                showStartPosition() })

        (viewModel as? MultiplayerViewModel)?.playersAnswer?.observe(
            this,
            Observer { showPlayersAnswer(it) })

    }


    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.setOnMapClickListener(this)
        map?.setOnCameraMoveListener(this)
    }

    override fun onMapClick(position: LatLng?) {
        if(position!=null){

            viewModel.clickedPosition(position.latitude, position.longitude)
        }
    }
    override fun onCameraMove() {

        viewModel.cameraMoved()
    }

    fun addMarks(position: LatLng?, distance: Int?){
        map?.addMarker(position?.let {
            MarkerOptions().position(it).title(distance.toString())
        })

    }
    private fun showStartPosition(){
        val defaultPosition = getStartPosition()
        val location = CameraUpdateFactory.newLatLngZoom(defaultPosition, 1.0f) // вынести переменную
        map?.animateCamera(location)
    }

    private fun showAnswer(
        task: CityTask,
        clickedPosition: LatLng,
        playersAnswers: List<LatLng>? = null
    ) {
        val answerLat = task.latitude ?: return
        val answerLon = task.longitude ?: return
        val answerPosition = LatLng(answerLat, answerLon)
        val answerMarket = MarkerOptions()
            .position(answerPosition)
            .title(task.city)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        map?.let{
            it.addMarker(answerMarket)
            zoomMarkets(listOf(answerPosition, clickedPosition) + (playersAnswers ?: ArrayList()))
        }
    }

    private fun showPlayersAnswer(playersAnswer: Map<String, Pair<Double, Double>?>) {
        playersAnswer
            .filterValues { it != null }
            .forEach {
                map?.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.value!!.first, it.value!!.second))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )
            }
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

    private fun getStartPosition(): LatLng {
        return startLocation ?: LatLng(0.0, 0.0)
    }

}

//operator fun List<LatLng>.plus(list: List<LatLng>?) : List<LatLng>{
//    list?.let{
//        return this + it
//    }
//    return this
//}

