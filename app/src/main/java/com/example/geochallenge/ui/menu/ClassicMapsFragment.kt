package com.example.geochallenge.ui.menu

import android.content.Context
import com.example.geochallenge.R

class ClassicMapsFragment : BaseMapsFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MenuActivity).supportActionBar?.title = "Solo"
        (activity as MenuActivity).supportActionBar?.subtitle = "Игра на очки"
    }

    override fun getLayout(): Int {
        return R.layout.fr_classic_maps
    }
}