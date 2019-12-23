package com.example.geochallenge.ui.menu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.ui.menu.rv.GameMapAdapter
import com.example.geochallenge.ui.menu.vm.MenuMapsViewModel

import javax.inject.Inject

abstract class BaseMapsFragment : Fragment() {

    abstract fun getLayout(): Int

    @Inject
    lateinit var adapterView: GameMapAdapter

    @Inject
    lateinit var viewModel: MenuMapsViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(getLayout(), container, false)
        recyclerView = v.findViewById(R.id.maps_rv)
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (context as MenuActivity).menuComponent?.inject(this)

        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = linearLayoutManager
        }

        recyclerView.adapter = adapterView

        viewModel.maps.observe(this, Observer {
            adapterView.add(it, true)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMaps()
    }
}