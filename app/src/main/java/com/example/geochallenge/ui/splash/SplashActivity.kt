package com.example.geochallenge.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.ui.auth.AuthActivity
import com.example.geochallenge.ui.menu.MenuActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 101
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser


        if (user == null) auth()
        else {
            (applicationContext as AppDelegate).createUserComponent(user)
            startGameMenu()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                showMessage("Вход прошел успешно")
                val user = FirebaseAuth.getInstance().currentUser
                (applicationContext as AppDelegate).createUserComponent(user!!)
                startGameMenu()
            }
        }
    }


    fun auth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun startGameMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}