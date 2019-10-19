package com.example.geochallenge.ui.records

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.Record

class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val pointsTv: TextView = itemView.findViewById(R.id.recordText)

    fun bind(record: Record){
        pointsTv.text = record.points.toString()

    }

}