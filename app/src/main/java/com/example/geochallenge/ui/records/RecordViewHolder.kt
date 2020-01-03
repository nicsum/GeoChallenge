package com.example.geochallenge.ui.records

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.Record

class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val pointsTv: TextView = itemView.findViewById(R.id.recordText)
    private val placeTv: TextView = itemView.findViewById(R.id.placeText)
    private val userTv: TextView = itemView.findViewById(R.id.userText)

    fun bind(record: Record, place: Int, isMyRecord: Boolean) {
        if (isMyRecord)
            itemView.setBackgroundColor(Color.YELLOW)
        else itemView.setBackgroundColor(Color.WHITE)
        pointsTv.text = record.score.toString()
        placeTv.text = place.toString()
        userTv.text = record.username
    }

}