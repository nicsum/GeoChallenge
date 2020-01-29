package com.example.geochallenge.ui.game

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.time.TimeLimitGameActivity
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.material.snackbar.Snackbar


abstract class BaseGameMapActivity : AppCompatActivity() {

    private val answerExitListener = object : AnswerExitListener {
        override fun onExit() {
            getViewModel().finishGame()
        }

        override fun onCancel() {}
    }

    private var tryFinishGameListener = object : FinishGameListener {

        override fun onShowRecords() {
            exitToRecords()
        }

        override fun onReplay() {
            replay()
        }

        override fun onExit() {
            hardExit()
        }
    }

    private var networkCallback: NetworkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : NetworkCallback() {
        override fun onAvailable(network: Network?) {
            hideNetworkIsNotAvailableMessage()
        }

        override fun onLost(network: Network?) {
            showNetworkIsNotAvailableMessage()
        }

        override fun onBlockedStatusChanged(network: Network, blocked: Boolean) {
            Log.i("BaseGameMapActivity@@@", "blocked = $blocked")
            super.onBlockedStatusChanged(network, blocked)
        }

    }


    private var isFirstStartActivity: Boolean = false
    private var snackbar: Snackbar? = null

    var activityComponent: GameActivityComponent? = null

    abstract fun getViewModel(): BaseGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        isFirstStartActivity = savedInstanceState == null

        setContentView(getLayout())

        listenNetworkStatus()

    }

    private fun listenNetworkStatus() {
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            cm.registerNetworkCallback(request, networkCallback)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cm.unregisterNetworkCallback(networkCallback)
        }
    }

    override fun onStart() {
        super.onStart()
        getViewModel().gameResult.observe(this, Observer {
            if (it != null) {
                showResultGameDialog()
            }
        })
//        getViewModel().isGameFinished.observe(this, Observer {
//            if (it) {
//                Toast.makeText(
//                    this,
//                    getString(R.string.game_is_finished),
//                    Toast.LENGTH_SHORT
//                ).show()
//                startActivity(Intent(this, RecordsActivity::class.java))
//                finish()
//            }
//        })

        getViewModel().error.observe(
            this,
            Observer {
                if (it == GameError.FINISH_GAME_ERROR) {
                    showTryAgainFinishGameDialog()
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

    private fun showNetworkIsNotAvailableMessage() {
        if (snackbar == null) {
            val view = findViewById<View>(R.id.game_view)
            snackbar = Snackbar.make(
                view,
                resources.getString(R.string.game_warning_internet_is_not_available),
                Snackbar.LENGTH_INDEFINITE
            )
        }
        snackbar?.show()
    }

    private fun hideNetworkIsNotAvailableMessage() {
        snackbar?.dismiss()
    }

    private fun replay() {
        val app = application as AppDelegate
        val gameInfo = app.gameComponent!!.getGameInfo()
        val gameMap = app.gameComponent!!.getGameMap()
        gameInfo.recordId = null
        (application as AppDelegate).destroyGameComponent()
        (application as AppDelegate).createGameComponent(gameInfo, gameMap)
        when (gameInfo.mode) {
            "solo" -> startActivity(Intent(this, ClassicGameActivity::class.java))
            "time" -> startActivity(Intent(this, TimeLimitGameActivity::class.java))
            else -> throw IllegalArgumentException("unknown mode")
        }
        finish()
    }

    private fun hardExit() {
        (application as AppDelegate).destroyGameComponent()
        finish()
    }

    private fun exitToRecords() {
        startActivity(Intent(this, RecordsActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        showAnswerExitDialog()
    }

    private fun showAnswerExitDialog() {
        AnswerExitDialog().show(supportFragmentManager, "AnswerExitDialog")
    }

    private fun showTryAgainFinishGameDialog() {
        TryFinishGameDialog().show(supportFragmentManager, "TryFinishGameDialog")
    }

    private fun showResultGameDialog() {
        ResultGameDialog().show(supportFragmentManager, "ResultGameDialog")
    }

    open fun getLayout() = R.layout.ac_game

    class AnswerExitDialog : DialogFragment() {

        private var listener: AnswerExitListener? = null

        override fun onAttach(context: Context) {
            super.onAttach(context)
            listener = (context as BaseGameMapActivity).answerExitListener
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
                    listener?.onExit()
                }.setNegativeButton(R.string.cancel) { _, _ ->
                    listener?.onCancel()
                }

            return builder.create()
        }
    }

    class TryFinishGameDialog : DialogFragment() {
        private var listener: FinishGameListener? = null
        override fun onAttach(context: Context) {
            super.onAttach(context)
            listener = (context as BaseGameMapActivity).tryFinishGameListener
        }

        override fun onDestroy() {
            super.onDestroy()
            listener = null
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = activity ?: return super.onCreateDialog(savedInstanceState)
            return AlertDialog.Builder(context)
                .setMessage(R.string.game_error_finish_game)
                .setPositiveButton(R.string.try_again) { _, _ ->
                    listener?.onShowRecords()
                }
                .setNegativeButton(R.string.exit) { _, _ ->
                    listener?.onExit()
                }
                .create()
        }
    }

    class ResultGameDialog : DialogFragment() {
        private var listener: FinishGameListener? = null
        private var score: Int? = null
        private var isMyBestResult: Boolean? = null
        override fun onAttach(context: Context) {
            super.onAttach(context)
            context as BaseGameMapActivity
            listener = context.tryFinishGameListener

            val vm = context.getViewModel()
            val result = vm.gameResult.value
            isMyBestResult = result?.second

            score = result?.first
        }

        override fun onDestroy() {
            super.onDestroy()
            listener = null
        }


        override fun onCancel(dialog: DialogInterface) {
            listener?.onExit()
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = activity ?: return super.onCreateDialog(savedInstanceState)
            val message = getMessage()
            return AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.leaderboard) { _, _ ->
                    listener?.onShowRecords()
                }.setNeutralButton(R.string.replay) { _, _ ->
                    listener?.onReplay()
                }
                .setNegativeButton(R.string.exit) { _, _ ->
                    listener?.onExit()
                }
                .create()
        }

        private fun getMessage(): String {
            return if (isMyBestResult!!) getString(
                R.string.result_game_message_with_congratulations,
                score
            )
            else getString(R.string.result_game_message, score)
        }
    }

}

