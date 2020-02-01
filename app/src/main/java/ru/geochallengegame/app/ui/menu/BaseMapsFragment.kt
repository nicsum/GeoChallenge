package ru.geochallengegame.app.ui.menu


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.geochallengegame.R
import ru.geochallengegame.app.ui.menu.rv.GameMapAdapter
import ru.geochallengegame.app.ui.menu.vm.MenuMapsViewModel

import javax.inject.Inject

abstract class BaseMapsFragment : Fragment() {

    abstract fun getLayout(): Int

    @Inject
    lateinit var adapterView: GameMapAdapter

    @Inject
    lateinit var viewModel: MenuMapsViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    private lateinit var recyclerView: RecyclerView

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

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapterView

        viewModel.maps.observe(viewLifecycleOwner, Observer {
            adapterView.add(it, true)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadMaps()
    }
}