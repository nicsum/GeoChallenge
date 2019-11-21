package com.example.geochallenge.ui.game

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.game.CityTask
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.reflect.KClass


class GameMapFragment : SupportMapFragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnCameraMoveListener  {

    var map: GoogleMap? = null
    var startLocation: LatLng? = null
//    var positionsMarkets : ArrayList<LatLng> = ArrayList() //TODO вынеси во вьюмодел. вообще следует переработать логику, вью модель не должна работать с гуглмаповскими объектами
    lateinit var viewModelClass : KClass<out SimpleGameViewModel>

    override fun onCreate(p0: Bundle?) {
        super.onCreate(p0)
        retainInstance = true
        getMapAsync(this)
    }

    //api 23?
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val typeGame = activity?.intent?.getStringExtra(GameActivity.TYPE_GAME_KEY) ?: GameActivity.DEFAULT_TYPE_GAME
        viewModelClass = when(typeGame){
            GameActivity.DEFAULT_TYPE_GAME -> SimpleGameViewModel::class
            GameActivity.CLASSIC_TYPE_GAME -> ClassicGameViewModel::class
            GameActivity.TIME_LIMIT_TYPE_GAME -> TimeLimitGameViewModel::class
            GameActivity.MULTIPLAYER_TYPE_GAME -> MultiplayerViewModel::class
            else -> SimpleGameViewModel::class
        }
        (activity?.intent?.getSerializableExtra(GameActivity.START_LOCATION_KEY) as Pair<Double, Double>).let {
            startLocation = LatLng(it.first, it.second)
        }

    }

    override fun onActivityCreated(p0: Bundle?) {
        super.onActivityCreated(p0)
        val viewModel =  ViewModelProviders.of(context as GameActivity).get(viewModelClass.java)
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

        if (viewModel is MultiplayerViewModel) {
            viewModel.playersAnswer.observe(this, Observer { showPlayersAnswer(it) })
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        map?.setOnMapClickListener(this)
        map?.setOnCameraMoveListener(this)
    }

    override fun onMapClick(position: LatLng?) {
        if(position!=null){
            val viewModel =  ViewModelProviders.of(context as GameActivity).get(viewModelClass.java)
            viewModel.clickedPosition(position.latitude, position.longitude)
        }
    }
    override fun onCameraMove() {
        val viewModel =  ViewModelProviders.of(context as GameActivity).get(viewModelClass.java)
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

