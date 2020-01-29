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
import com.example.geochallenge.game.GameInfo
import javax.inject.Inject

class RecordsFragment @Inject constructor() : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingView: View

    @Inject
    lateinit var viewModel: RecordsViewModel

    @Inject
    lateinit var gameInfo: GameInfo

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var recordsAdapter: RecordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fr_records, container, false)
        recyclerView = v.findViewById(R.id.records_rv)
        loadingView = v.findViewById(R.id.loading)
        initAdapter()
//        initState()
        return v
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.records.observe(
            viewLifecycleOwner,
            Observer {
                recordsAdapter.add(it, true)
                recyclerView.smoothScrollToPosition(
                    it.indexOfFirst { record ->
                        record.id == gameInfo.recordId
                    })
            })
    }

    private fun initAdapter() {
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = recordsAdapter

    }
//
//    private fun initState() {
//        viewModel.getState().observe(this, Observer { state ->
//            loading.visibility = if (viewModel.listIsEmpty() && state == RecordsDataSource.State.LOADING) View.VISIBLE else View.GONE
////            errorText.visibility = if (viewModel.listIsEmpty() && state == RecordsDataSource.State.ERROR) View.VISIBLE else View.GONE
//            if (!viewModel.listIsEmpty()) {
//                recordsAdapter.setState(state ?: RecordsDataSource.State.DONE)
//            }
//        })
//    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRecords()
    }

}