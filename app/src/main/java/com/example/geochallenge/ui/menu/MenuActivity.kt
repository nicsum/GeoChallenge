package com.example.geochallenge.ui.menu


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ac_menu)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if(fragment == null){
            fragment = MenuFragment()
            changeFragment(fragment)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseAuth.getInstance().currentUser !=null)
            menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.logout){
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun logout() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    (applicationContext as AppDelegate).destroyUserComponent()
                    splash()
                    finish()
                } else (showMessage("Что-то пошло не так. Попробуйте еще раз"))
            }
    }


    fun splash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
    }


    private fun changeFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, newFragment)
            .commit()
    }


    fun showMessage(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}