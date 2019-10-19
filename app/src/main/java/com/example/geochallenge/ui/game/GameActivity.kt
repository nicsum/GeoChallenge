package com.example.geochallenge.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.geochallenge.R
import com.example.geochallenge.ui.records.RecordsActivity


class GameActivity : AppCompatActivity() {


    var isFirstStartActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_game)

        isFirstStartActivity = savedInstanceState == null

        val viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)
        viewModel.gameFinished.observe(this , Observer {
            if(it){
                Toast.makeText(this, "Игра окончена", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                finish()
            }
        })
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
        if(isFirstStartActivity)
            ViewModelProviders.of(this).get(GameViewModel::class.java).newGame()
    }

    override fun onPause() {
        super.onPause()
        isFirstStartActivity = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ViewModelProviders.of(this).get(GameViewModel::class.java).finishGame()
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