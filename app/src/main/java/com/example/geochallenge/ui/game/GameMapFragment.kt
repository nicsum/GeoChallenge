package com.example.geochallenge.ui.game

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.game.TaskAnswer
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


class GameMapFragment : SupportMapFragment(),
    OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener {

    var map: GoogleMap? = null

    lateinit var viewModel: BaseGameViewModel

    @Inject
    lateinit var gameMap: GameMap

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        retainInstance = true
        getMapAsync(this)
    }


    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)

        (activity as BaseGameMapActivity).activityComponent?.inject(this)
        viewModel = (activity as BaseGameMapActivity).getViewModel()

        viewModel.isDefaultMapState.observe(this, Observer {
            Log.i("GameMapFragment", "isDefaultMapState = $it")
            if(it){
                map?.clear()
                map?.setOnMapClickListener(this)
                showStartPosition()
            }
        })
        viewModel.clickedPosition.observe(this,
            Observer {it?.let { addMarks(LatLng(it.first, it.second) , viewModel.distance.value) }
            })
        viewModel.taskAnswer.observe(this, Observer { answer ->
            if (answer != null) {
                map?.setOnMarkerClickListener { false}
                showAnswer(answer)
            }
            else {
                map?.setOnMarkerClickListener { true}
            }  })

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
            map?.setOnMapClickListener(null)
        }
    }
    override fun onCameraMove() {

        viewModel.cameraMoved()
    }

    fun addMarks(position: LatLng?, distance: Double?) {
        map?.addMarker(position?.let {
            MarkerOptions().position(it).title(distance.toString())
        })

    }
    private fun showStartPosition(){
        val defaultPosition = getStartPosition()
        val zoom = gameMap.zoom?.toFloat() ?: 1.0f
        val location = CameraUpdateFactory.newLatLngZoom(defaultPosition, zoom)
        map?.animateCamera(location)
    }

    private fun showAnswer(
        taskAnswer: TaskAnswer
    ) {
        val playersAnswers = taskAnswer.playersAnswers
            ?.values
            ?.filterNotNull()
        val answerPosition = LatLng(taskAnswer.task.latitude!!, taskAnswer.task.longitude!!)
        val answerMarket = MarkerOptions()
            .position(answerPosition)
            .title(taskAnswer.task.name)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

        map?.let{
            it.addMarker(answerMarket)
            zoomMarkets(
                listOfNotNull(answerPosition, taskAnswer.answer) + (playersAnswers ?: ArrayList())
            )
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
        return LatLng(gameMap.latitude ?: 0.0, gameMap.longitude ?: 0.0)
    }

}

//operator fun List<LatLng>.plus(list: List<LatLng>?) : List<LatLng>{
//    list?.let{
//        return this + it
//    }
//    return this
//}

//.playersAnswer
//?.value
//?.values
//?.filterNotNull()
//?.map { coordinates -> LatLng(coordinates.first, coordinates.second) }


