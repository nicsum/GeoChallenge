package com.example.geochallenge.ui.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.geochallenge.R
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity(){

    companion object{

        const val RC_SIGN_IN = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ac_menu)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if(fragment == null){
            fragment = MenuFragment()
            changeFragment(fragment)
        }

        val user = FirebaseAuth.getInstance().currentUser
        if(user == null) login()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            if(resultCode == Activity.RESULT_OK){
                showMessage("Вход прошел успешно")
            }
        }
    }
    private fun changeFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, newFragment)
            .commit()
    }

    fun logout(){
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                if(it.isSuccessful)login()
                else(showMessage("Что-то пошло не так. Попробуйте еще раз"))
            }
    }

    fun login(){
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

    fun showMessage(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}