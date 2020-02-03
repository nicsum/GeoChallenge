package ru.geochallengegame.app.ui.game.endless

import android.os.Bundle
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.ui.game.AnswerExitListener
import ru.geochallengegame.app.ui.game.BaseGameMapActivity
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import javax.inject.Inject

class EndlessGameActivity : BaseGameMapActivity() {
    @Inject
    lateinit var fragment: EndlessGameInfoFragment

    @Inject
    lateinit var viewModel: EndlessGameViewModel

    override val answerExitListener: AnswerExitListener
        get() = object : AnswerExitListener {
            override fun onExit() {
                hardExit()
            }

            override fun onCancel() {}
        }

    override fun showResultGameDialog() {
        showAnswerExitDialog()
    }

    override fun getViewModel(): BaseGameViewModel {
        return viewModel
    }

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
}