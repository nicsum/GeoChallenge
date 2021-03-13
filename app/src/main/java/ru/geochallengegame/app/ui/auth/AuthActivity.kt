package ru.geochallengegame.app.ui.auth

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.di.auth.AuthComponent
import ru.geochallengegame.app.ui.menu.MenuActivity
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
        viewModel.forgotPasswordScreen.observe(
            this,
            Observer { if (it) showForgotPasswordScreen() }
        )
        viewModel.loginScreen.observe(
            this,
            Observer { if (it) showLoginScreen() }
        )
        viewModel.registrationScreen.observe(
            this,
            Observer { if (it) showRegistrationScreen() }
        )
    }

    private fun showLoginScreen() {

        val loginFragment = LoginFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, loginFragment)
            .commit()
    }

    private fun showRegistrationScreen() {
        val registrationFragment =
            RegistrationFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, registrationFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showForgotPasswordScreen() {

        val forgotPasswordFragment =
            ForgotPasswordFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, forgotPasswordFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        supportFragmentManager.backStackEntryCount.also {
            if (it == 0) super.onBackPressed()
            else {
                supportFragmentManager.popBackStack()
                viewModel.showLoginScreen()
            }
        }
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
        if (error == AuthErrors.NONE) return
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
            return AlertDialog.Builder(context, R.style.DialogTheme)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    (context as? AuthActivity)?.viewModel?.iKnowAboutAuthError()
                }
                .create()
        }

    }


}