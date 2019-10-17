package com.example.geochallenge.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil

class GameActivity : AppCompatActivity() {


    var isFirstStartActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_game)

        isFirstStartActivity = savedInstanceState == null
//        if (savedInstanceState == null){
//            val mapFragment: SupportMapFragment = supportFragmentManager
//                .findFragmentById(R.id.map) as SupportMapFragment
//
//            mapFragment.retainInstance = true
//
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.game_info_container, GameInfoFragment())
//                .commit()
//
//        }

    }

    override fun onResume() {
        super.onResume()
        if(isFirstStartActivity)  ViewModelProviders.of(this).get(GameViewModel::class.java).newGame()
    }

    //OnMapReadyCallback
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        initMap()
//
//    }

//    fun initMap(){
//        map.setOnMapClickListener {
//            map.clear()
//            map.addMarker(MarkerOptions()
//                .position(it))
//            updateDistantion(it)
//        }
//    }

//    fun updateDistantion(position: LatLng){
//         val fragment = supportFragmentManager
//             .findFragmentById(R.id.game_info_container) as GameInfoFragment
//
//
//
//        fragment.setDistance(distance.toInt()/1000)
//    }





//    fun changeFragmant(newFragment: Fragment){
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.map_container, newFragment)
//            .commit()
//    }
}