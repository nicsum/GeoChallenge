package com.example.geochallenge.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geochallenge.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil

class GameActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var map: GoogleMap

    val moscowPosition : LatLng = LatLng(55.4507 ,  37.3656)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_game)

        if (savedInstanceState == null){
            val mapFragment: SupportMapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

            mapFragment.retainInstance = true
            mapFragment.getMapAsync(this)

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.game_info_container, GameInfoFragment())
                .commit()

        }

    }

    //OnMapReadyCallback
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        initMap()

    }

    fun initMap(){
        map.setOnMapClickListener {
            map.clear()
            map.addMarker(MarkerOptions()
                .position(it))
            updateDistantion(it)
        }
    }

    fun updateDistantion(position: LatLng){
         val fragment = supportFragmentManager
             .findFragmentById(R.id.game_info_container) as GameInfoFragment

        val distance = SphericalUtil.computeDistanceBetween(moscowPosition, position)

        fragment.setDistance(distance.toInt()/1000)
    }





//    fun changeFragmant(newFragment: Fragment){
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.map_container, newFragment)
//            .commit()
//    }
}