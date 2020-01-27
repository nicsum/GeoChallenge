package com.example.geochallenge.ui.menu

import com.example.geochallenge.R

class TimeMapsFragment : BaseMapsFragment() {
    override fun getLayout(): Int {
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.time_title)
        (activity as MenuActivity).supportActionBar?.subtitle = getString(R.string.time_subtitle)
        return R.layout.fr_time_maps
    }

}