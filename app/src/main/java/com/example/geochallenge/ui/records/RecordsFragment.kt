package com.example.geochallenge.ui.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R

class RecordsFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var loadingView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_records, container, false)
        recyclerView = v.findViewById(R.id.records_rv)
        loadingView = v.findViewById(R.id.loading)

        recyclerView.layoutManager = LinearLayoutManager(v.context)
        val adapter = RecordsAdapterView()
        recyclerView.adapter = adapter
//        AppDelegate.recordsStorage.getRecords()
//            .subscribe({
//            adapter.add(it, true)
//        },{
//                Log.d("fragment", it.message)
//            })


        return v
    }


}