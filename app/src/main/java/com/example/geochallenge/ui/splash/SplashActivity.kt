package com.example.geochallenge.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.menu.MenuActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) login()
        else {
            (applicationContext as AppDelegate).createUserComponent(user)
            startMenu()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                showMessage("Вход прошел успешно")
                val user = FirebaseAuth.getInstance().currentUser
                (applicationContext as AppDelegate).createUserComponent(user!!)
                startMenu()
            }
        }
    }


    fun login() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build()
//            AuthUI.IdpConfig.PhoneBuilder().build(),
//            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.ic_menu_single)
                .build(),
            RC_SIGN_IN
        )
    }

    fun startMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}