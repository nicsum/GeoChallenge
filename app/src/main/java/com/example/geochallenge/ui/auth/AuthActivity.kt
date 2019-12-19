package com.example.geochallenge.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.auth.AuthComponent
import com.example.geochallenge.ui.menu.MenuActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    }

    fun showLoginScreen() {
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

    fun startGameMenu() {
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
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.e("ad", e.message)
            }

        }
    }


    //TODO мб вынести во viewmodel
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startGameMenu()
                }
            }
    }

}