package com.example.geochallenge.ui.menu

import com.example.geochallenge.R

class TimeMapsFragment : BaseMapsFragment() {
    override fun getLayout(): Int {
        (activity as MenuActivity).supportActionBar?.title = "Time Trial"
        (activity as MenuActivity).supportActionBar?.subtitle = "Игра на время"
        return R.layout.fr_time_maps
    }

}