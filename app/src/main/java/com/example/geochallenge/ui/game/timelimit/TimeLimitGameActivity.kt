package com.example.geochallenge.ui.game.timelimit

import android.os.Bundle
import com.example.geochallenge.R
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.time.TimeGameComponent
import com.example.geochallenge.di.time.TimeGameModule
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel


class TimeLimitGameActivity : BaseGameMapActivity() {

    val timeComponent: TimeGameComponent by lazy {
        activityComponent.provideTimeGameComponent(TimeGameModule())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()
    }

    override fun getLayout(): Int {
        return R.layout.ac_game
    }

    override fun getViewModel(): BaseGameViewModel {
        return (supportFragmentManager
            .findFragmentById(R.id.game_info_container) as BaseGameInfoFragment).viewModel
    }

    override fun getMapComponent(): MapComponent {
        return timeComponent
    }
}