package com.example.geochallenge.ui.menu.rv

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.menu.OnClickMapListener

class GameMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var map: GameMap

    fun bind(map: GameMap, listener: OnClickMapListener) {
        this.map = map
        val mapTv = itemView.findViewById<TextView>(R.id.mapText)
        mapTv.text = map.mapRu
        itemView.setOnClickListener {
            listener.onClickGameMap(map)
        }
    }
}