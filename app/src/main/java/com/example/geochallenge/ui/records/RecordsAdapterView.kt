package com.example.geochallenge.ui.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.Record

class RecordsAdapterView : RecyclerView.Adapter<RecordViewHolder>() {


    var records : MutableList<Record> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecordViewHolder(inflater.inflate(R.layout.li_record, parent, false))
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(records[position])
    }

    fun add(records: MutableList<Record>, refresh: Boolean){
        if(refresh){
            this.records = ArrayList(records.size)
        }
        this.records.addAll(records)
        this.records.sortDescending()
        notifyDataSetChanged()
    }
}