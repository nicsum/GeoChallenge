package ru.geochallengegame.app.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.ui.auth.AuthActivity
import ru.geochallengegame.app.ui.menu.MenuActivity

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

    private fun startGameMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }


}