package com.example.geochallenge.ui.game.multiplayer

import android.os.Bundle
import com.example.geochallenge.R
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.multiplayer.MultiplayerGameComponent
import com.example.geochallenge.di.multiplayer.MultiplayerGameModule
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel


class MultiplayerGameActivity : BaseGameMapActivity() {

    val multiplayerComponent: MultiplayerGameComponent by lazy {
        val countTasks = getGameInfo().countTaskForLevel
        activityComponent
            .provideMultiplayerGameComponent(MultiplayerGameModule(countTasks))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()
    }

    override fun getMapComponent(): MapComponent {
        return multiplayerComponent
    }

    override fun getLayout(): Int {
        return R.layout.ac_game
    }

    override fun getViewModel(): BaseGameViewModel {
        return (supportFragmentManager
            .findFragmentById(R.id.game_info_container) as BaseGameInfoFragment).viewModel
    }
}