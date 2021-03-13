package ru.geochallengegame.app.ui.game.immortal

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.ui.game.AnswerExitListener
import ru.geochallengegame.app.ui.game.BaseGameMapActivity
import ru.geochallengegame.app.ui.game.BaseGameViewModel
import javax.inject.Inject

class ImmortalGameActivity : BaseGameMapActivity() {
    @Inject
    lateinit var fragment: ImmortalGameInfoFragment

    @Inject
    lateinit var viewModel: ImmortalGameViewModel

    override val answerExitListener: AnswerExitListener
        get() = object : AnswerExitListener {
            override fun onExit() {
                exit()
            }

            override fun onCancel() {}
        }

    override fun showResultGameDialog() {
        ResultGameDialog().show(supportFragmentManager, "ResultGameDialog")
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

    private fun exit() {
        hardExit()
    }


    class ResultGameDialog : DialogFragment() {

        override fun onCancel(dialog: DialogInterface) {
            (activity as ImmortalGameActivity).exit()
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = activity as ImmortalGameActivity
            return AlertDialog.Builder(context, R.style.DialogTheme)
                .setMessage(R.string.result_game_message_level_are_over)
                .setPositiveButton(R.string.exit) { _, _ ->
                    (activity as ImmortalGameActivity).exit()
                }
                .create()
        }


    }
}
