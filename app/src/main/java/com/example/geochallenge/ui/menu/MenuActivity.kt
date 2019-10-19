package com.example.geochallenge.ui.menu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geochallenge.R

class MenuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ac_menu)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if(fragment == null){
            fragment = MenuFragment()
            changeFragment(fragment)
        }
    }

    private fun changeFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, newFragment)
            .commit()
    }


}