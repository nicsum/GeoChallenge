package com.example.geochallenge.ui.game.multiplayer

import android.os.Bundle
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import javax.inject.Inject


class MultiplayerGameActivity : BaseGameMapActivity() {

    @Inject
    lateinit var fragment: MultiplayerGameInfoFragment

    @Inject
    lateinit var viewModel: MultiplayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {


        val gi = getGameInfo("mp", getMapId())
        gameComponent = (application as AppDelegate)
            .userComponent!!.gameComponent()
            .create(gi, getStartLocation(), this)

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