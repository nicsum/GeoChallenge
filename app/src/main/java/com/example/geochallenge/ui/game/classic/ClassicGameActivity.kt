package com.example.geochallenge.ui.game.classic

import android.os.Bundle
import com.example.geochallenge.R
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.classic.ClassicGameComponent
import com.example.geochallenge.di.classic.ClassicGameModule
import com.example.geochallenge.ui.game.BaseGameInfoFragment
import com.example.geochallenge.ui.game.BaseGameMapActivity
import com.example.geochallenge.ui.game.BaseGameViewModel

class ClassicGameActivity : BaseGameMapActivity() {


    val classicComponent: ClassicGameComponent by lazy {
        activityComponent.provideClassicGameComponent(ClassicGameModule())
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
        return classicComponent
    }
}