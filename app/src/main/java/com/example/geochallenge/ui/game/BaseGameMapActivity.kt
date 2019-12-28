package com.example.geochallenge.ui.game

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.activity.GameActivityComponent
import com.example.geochallenge.net.NetworkChangeReceiver
import com.example.geochallenge.net.NetworkIsChangeListener
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.material.snackbar.Snackbar


abstract class BaseGameMapActivity : AppCompatActivity() {

    private val exitListener = object : AnswerExitListener {
        override fun OnExit() {
            getViewModel().finishGame()
        }

        override fun OnCancel() {}
    }

    private var tryFinishGameListener = object : TryFinishGameListener {
        override fun onFinishGame() {
            getViewModel().finishGame()
        }

        override fun onExit() {
            hardExit()
        }
    }

    var networkCallback: NetworkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    object : NetworkCallback() {
        override fun onAvailable(network: Network?) {
            hideNetworkIsNotAvailableMessage()
        }

        override fun onLost(network: Network?) {
            showNetworkIsNotAvailableMessage()
        }
    }

    var networkChangeReceiver: NetworkChangeReceiver? = null

    var isFirstStartActivity: Boolean = false

    var activityComponent: GameActivityComponent? = null
    var snackbar: Snackbar? = null

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
        } else {
            networkChangeReceiver = NetworkChangeReceiver()
            val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
            registerReceiver(networkChangeReceiver, filter)
            networkChangeReceiver?.bind(object : NetworkIsChangeListener {
                override fun onChange(isAvailable: Boolean) {
                    if (isAvailable) hideNetworkIsNotAvailableMessage()
                    else showNetworkIsNotAvailableMessage()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cm.unregisterNetworkCallback(networkCallback)
        } else {
            unregisterReceiver(networkChangeReceiver)
            networkChangeReceiver?.unbind()
            networkChangeReceiver = null
        }

    }

    override fun onStart() {
        super.onStart()
        getViewModel().isGameFinished.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.game_is_finished),
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                finish()
            }
        })

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

    private fun hardExit() {
        (application as AppDelegate).destroyGameComponent()
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

    open fun getLayout() = R.layout.ac_game

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

    class TryFinishGameDialog : DialogFragment() {
        var listener: TryFinishGameListener? = null
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
                    listener?.onFinishGame()
                }
                .setNegativeButton(R.string.exit) { _, _ ->
                    listener?.onExit()
                }
                .create()
        }
    }

}

