package com.example.geochallenge.ui.menu

import android.content.Context
import com.example.geochallenge.R

class ClassicMapsFragment : BaseMapsFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.solo_title)
        (activity as MenuActivity).supportActionBar?.subtitle = getString(R.string.solo_subtitle)
    }

    override fun getLayout(): Int {
        return R.layout.fr_classic_maps
    }
}