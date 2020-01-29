package com.example.geochallenge.ui.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.auth.AuthComponent
import com.example.geochallenge.ui.menu.MenuActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


class AuthActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 9001
    }

    @Inject
    lateinit var viewModel: AuthViewModel
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var authComponent: AuthComponent

    private lateinit var loadingView: View
    private lateinit var screenView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        authComponent = (applicationContext as AppDelegate)
            .appComponent.authComponent()
            .create(this)
        authComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_auth)
        if (savedInstanceState == null) {
            showLoginScreen()
        }

        loadingView = findViewById(R.id.loading_view)
        screenView = findViewById(R.id.container)
    }

    override fun onStart() {
        super.onStart()
        viewModel.isSignIn.observe(
            this,
            Observer {
                if (it) startGameMenu()
            })

        viewModel.isPasswordReset.observe(
            this,
            Observer {
                if (it) showLoginScreen()
            }
        )

        viewModel.loadingIsVisible.observe(
            this,
            Observer {
                if (it) showLoading()
                else hideLoading()
            })


    }

    private fun showLoginScreen() {
        viewModel.clearField()
        val loginFragment = LoginFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, loginFragment)
            .commit()
    }

    fun showRegistrationScreen() {
        viewModel.clearField()
        val registrationFragment = RegistrationFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, registrationFragment)
            .addToBackStack(null)
            .commit()
    }

    fun showForgotPasswordScreen() {
        viewModel.clearField()
        val forgotPasswordFragment = ForgotPasswordFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, forgotPasswordFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun startGameMenu() {
        if (!createUserComponent()) return
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()

    }

    private fun createUserComponent(): Boolean {
        val user = firebaseAuth.currentUser ?: return false
        if (firebaseAuth.currentUser != null) (applicationContext as AppDelegate).createUserComponent(
            user
        )
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (requestCode == RC_SIGN_IN) {
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.authWithGoogle(account!!)
            } catch (e: ApiException) {
                if (e.statusCode == CommonStatusCodes.NETWORK_ERROR) viewModel.authError.postValue(
                    AuthErrors.CONNECTION_FAILED
                )
                else viewModel.authError.postValue(AuthErrors.ANY)
            }

        }
    }

    override fun onPause() {
        super.onPause()
        val fragment = supportFragmentManager.findFragmentByTag("ErrorMessageDialog")
        if (fragment != null)
            supportFragmentManager.beginTransaction().remove(fragment).commit()
    }

    private fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingView.visibility = View.GONE
    }

    fun showAuthError(message: String) {
        AuthErrorMessageDialog()
            .errorMessage(message)
            .show(supportFragmentManager, "ErrorMessageDialog")
    }

    fun showAuthError(error: AuthErrors) {
        val message = when (error) {
            AuthErrors.CONNECTION_FAILED -> R.string.auth_error_connection
            AuthErrors.TO_MANY_REQUESTS -> R.string.auth_error_too_many_requests
            AuthErrors.INVALID_USER -> R.string.invalid_user_warning
            else -> R.string.auth_error
        }.let { getString(it) }
        AuthErrorMessageDialog()
            .errorMessage(message)
            .show(supportFragmentManager, "ErrorMessageDialog")
    }

    class AuthErrorMessageDialog : DialogFragment() {

        fun errorMessage(message: String): AuthErrorMessageDialog {
            this.message = message
            return this
        }

        private var message: String? = null
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context = activity ?: return super.onCreateDialog(savedInstanceState)
//            val authError = (context as? AuthActivity)?.viewModel?.authError?.value
//
            return AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    (context as? AuthActivity)?.viewModel?.iKnowAboutAuthError()
                }
                .create()
        }

    }


}