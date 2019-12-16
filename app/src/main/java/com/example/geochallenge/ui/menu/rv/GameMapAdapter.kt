package com.example.geochallenge.ui.menu.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.menu.OnClickMapListener
import javax.inject.Inject

class GameMapAdapter @Inject constructor(val listener: OnClickMapListener) :
    RecyclerView.Adapter<GameMapViewHolder>() {

    private val maps = ArrayList<GameMap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameMapViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GameMapViewHolder(inflater.inflate(R.layout.li_map, parent, false))
    }

    override fun getItemCount(): Int {
        return maps.size
    }

    override fun onBindViewHolder(holder: GameMapViewHolder, position: Int) {
        holder.bind(maps.get(position), listener)
    }

    fun add(maps: List<GameMap>, refresh: Boolean) {
        if (refresh) {
            this.maps.clear()
        }
        this.maps.addAll(maps)
        notifyDataSetChanged()
    }
}