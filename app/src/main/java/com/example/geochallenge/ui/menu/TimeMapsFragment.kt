package com.example.geochallenge.ui.menu

import android.os.Bundle
import com.example.geochallenge.R

class TimeMapsFragment : BaseMapsFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.time_title)
        (activity as MenuActivity).supportActionBar?.subtitle = getString(R.string.time_subtitle)
    }

    override fun getLayout(): Int {

        return R.layout.fr_time_maps
    }

}