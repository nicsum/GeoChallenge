package ru.geochallengegame.app.ui.game

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ru.geochallengegame.R
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.game.TaskAnswer
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
        viewModel.isDefaultMapState.observe(viewLifecycleOwner, Observer {
            if(it){
                map?.clear()
                map?.setOnMapClickListener(this)
                showStartPosition()
            }
        })
        viewModel.clickedPosition.observe(
            viewLifecycleOwner,
            Observer {it?.let { addMarks(LatLng(it.first, it.second) , viewModel.distance.value) }
            })

        viewModel.taskAnswer.observe(viewLifecycleOwner, Observer { answer ->
            if (answer != null) {
                map?.setOnMarkerClickListener { false}
                showAnswer(answer)
            }
            else {
                map?.setOnMarkerClickListener { true}
            }
        })

        viewModel.isTaskCompleted.observe(viewLifecycleOwner,
            Observer {
                if (it) map?.setOnMapClickListener(null)
                else map?.setOnMapClickListener(this)
            })

        viewModel.isGameFinished.observe(viewLifecycleOwner,
            Observer {
                if (it) map?.setOnMapClickListener(null)
                else map?.setOnMapClickListener(this)
            })

    }

    override fun onMapReady(map: GoogleMap?) {

        this.map = map
        val customStyle = gameMap.style
        this.map?.apply {
            setOnMapClickListener(this@GameMapFragment)
            setOnCameraMoveListener(this@GameMapFragment)
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMapToolbarEnabled = false

            if (customStyle == null) {
                setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.style_json
                    )
                )
            } else setMapStyle(MapStyleOptions(customStyle))
        }
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

    private fun addMarks(position: LatLng?, distance: Double?) {
        map?.addMarker(position?.let {
            MarkerOptions().position(it).title(distance.toString())
        })

    }
    private fun showStartPosition(){
        val defaultPosition = getStartPosition()
        val zoom = getDefaultZoom()
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
            val zoom = getDefaultZoom()
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(positionsMarkets[0], zoom))
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

    private fun getDefaultZoom(): Float {
        return gameMap.zoom?.toFloat() ?: 1.0f
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


