package com.example.geochallenge.ui.menu

import com.example.geochallenge.R

class ClassicMapsFragment : BaseMapsFragment() {
    override fun getLayout(): Int {
        (activity as MenuActivity).supportActionBar?.title = "Solo"
        (activity as MenuActivity).supportActionBar?.subtitle = "Игра на очки"
        return R.layout.fr_classic_maps
    }
}