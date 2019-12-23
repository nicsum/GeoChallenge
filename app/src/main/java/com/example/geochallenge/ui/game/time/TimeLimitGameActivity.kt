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

        activityComponent = (application as AppDelegate)
            .gameComponent
            ?.gameActivityComponent()
            ?.create(this)
        activityComponent?.inject(this)

        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

}