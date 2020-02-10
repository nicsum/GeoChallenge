package ru.geochallengegame.app.ui.menu.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.geochallengegame.R
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.ui.menu.OnClickMapListener
import javax.inject.Inject

class GameMapAdapter @Inject constructor(private val listener: OnClickMapListener) :
    RecyclerView.Adapter<GameMapViewHolder>() {

    private val maps = ArrayList<GameMap>()

    var modeWithLeaderboard: Boolean = true
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameMapViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GameMapViewHolder(
            inflater.inflate(
                R.layout.li_map,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return maps.size
    }

    override fun onBindViewHolder(holder: GameMapViewHolder, position: Int) {
        holder.bind(modeWithLeaderboard, maps[position], listener)
    }

    fun add(maps: List<GameMap>, refresh: Boolean) {
        if (refresh) {
            this.maps.clear()
        }
        this.maps.addAll(maps)
        notifyDataSetChanged()
    }


}