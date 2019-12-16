package com.example.geochallenge.ui.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import javax.inject.Inject

class RecordsFragment @Inject constructor() : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var loadingView: View

    @Inject
    lateinit var adapterView: RecordsAdapterView

    @Inject
    lateinit var viewModel: RecordsViewModel

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_records, container, false)
        recyclerView = v.findViewById(R.id.records_rv)
        loadingView = v.findViewById(R.id.loading)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapterView
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.records.observe(this, Observer {
            adapterView.add(it, true)
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRecords()
    }

}