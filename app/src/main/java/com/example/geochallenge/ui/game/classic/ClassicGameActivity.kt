package com.example.geochallenge.ui.game.classic

import android.os.Bundle
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel
import javax.inject.Inject

class ClassicGameActivity : BaseGameMapActivity() {


    @Inject
    lateinit var fragment: ClassicGameInfoFragment

    @Inject
    lateinit var viewModel: ClassicGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {


        val gi = getGameInfo("solo", getMapId())
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