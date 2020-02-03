package ru.geochallengegame.app.ui.menu

import android.os.Bundle
import ru.geochallengegame.R

class EndlessMapsFragment : BaseMapsFragment() {


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.endless_title)
        (activity as MenuActivity).supportActionBar?.subtitle = getString(R.string.endless_subtitle)
    }

    override fun getLayout(): Int {
        return R.layout.fr_classic_maps
    }
}