package com.example.geochallenge.ui.game.time

import android.os.Bundle
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import javax.inject.Inject


class TimeLimitGameActivity : BaseGameMapActivity() {

    @Inject
    lateinit var fragment: TimeLimitGameInfoFragment

    @Inject
    lateinit var viewModel: TimeLimitGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        val gi = getGameInfo("time", getMapId())
        gameComponent = (application as AppDelegate)
            .userComponent!!.gameComponent()
            .create(gi, getStartLocation(), this)
        gameComponent.inject(this)
        
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()
    }

    override fun getLayout(): Int {
        return R.layout.ac_game
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

}