package com.example.geochallenge.ui.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.Record
import javax.inject.Inject

class RecordsAdapterView @Inject constructor(val gameInfo: GameInfo) :
    RecyclerView.Adapter<RecordViewHolder>() {

    private var records = ArrayList<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RecordViewHolder(inflater.inflate(R.layout.li_record, parent, false))
    }

    override fun getItemCount(): Int {
        return records.size
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        val isMyRecord = (record.id != null && record.id == gameInfo.recordId)
        holder.bind(record, position + 1, isMyRecord)
    }

    fun add(records: List<Record>, refresh: Boolean) {
        if(refresh){
            this.records = ArrayList(records.size)
        }
        this.records.addAll(records)
        this.records.sortDescending()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}