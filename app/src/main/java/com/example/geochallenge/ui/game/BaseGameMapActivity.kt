package com.example.geochallenge.ui.game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.geochallenge.R
import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.ui.records.RecordsActivity


abstract class BaseGameMapActivity : AppCompatActivity() {

    private var exitListener = object : AnswerExitListener {
        override fun OnExit() {
            getViewModel().finishGame()
        }

        override fun OnCancel() {

        }
    }

    var isFirstStartActivity: Boolean = false

    var activityComponent: GameActivityComponent? = null

    abstract fun getLayout(): Int
    abstract fun getViewModel(): BaseGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        isFirstStartActivity = savedInstanceState == null

        setContentView(getLayout())
    }


    override fun onStart() {
        super.onStart()
        getViewModel().isGameFinished.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Игра окончена", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isFirstStartActivity)
            getViewModel().newGame()
    }

    override fun onPause() {
        super.onPause()
        isFirstStartActivity = false
    }

    override fun onBackPressed() {
        showAnswerExitDialog()
    }

    private fun showAnswerExitDialog() {
        AnswerExitDialog().show(supportFragmentManager, "AnswerExitDialog")
    }

    class AnswerExitDialog : DialogFragment() {

        var listener: AnswerExitListener? = null

        override fun onAttach(context: Context) {
            super.onAttach(context)
            listener = (context as BaseGameMapActivity).exitListener
        }

        override fun onDestroy() {
            super.onDestroy()
            listener = null
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = activity ?: return super.onCreateDialog(savedInstanceState)
            val builder = AlertDialog.Builder(context)
            builder
                .setMessage(R.string.answer_exit)
                .setPositiveButton(R.string.yes) { _, _ ->
                    listener?.OnExit()
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    listener?.OnCancel()
                }

            return builder.create()
        }
    }

}

