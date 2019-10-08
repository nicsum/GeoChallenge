package com.example.geochallenge.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geochallenge.R

abstract class BaseActivity : AppCompatActivity() {

    @Override
    open fun getLayout(): Int = R.layout.ac_menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayout())

        val fragment = supportFragmentManager.findFragmentById(R.id.container)

        if(fragment == null) changeFragmant(getStartFragment())

    }

    abstract fun  getStartFragment(): Fragment

    fun changeFragmant(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, newFragment)
            .commit()
    }



}