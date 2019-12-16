package com.example.geochallenge.ui.menu


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.menu.MenuComponent
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.multiplayer.MultiplayerGameActivity
import com.example.geochallenge.ui.game.street.StreetGameActivity
import com.example.geochallenge.ui.game.time.TimeLimitGameActivity
import com.example.geochallenge.ui.records.RecordsActivity
import com.example.geochallenge.ui.splash.SplashActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity(), OnClickMapListener {

//    private lateinit var appBarConfiguration: AppBarConfiguration

    var menuComponent: MenuComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        menuComponent = (applicationContext as AppDelegate)
            .userComponent
            ?.menuComponent()
            ?.create(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.ac_menu)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

//        appBarConfiguration = AppBarConfiguration(
//            setOf(R.id.nav_solo, R.id.nav_time, R.id.nav_settings), drawerLayout
//        )

//        var fragment = supportFragmentManager.findFragmentById(R.id.container)
//
//        if(fragment == null){
//            fragment = MenuFragment()
//            changeFragment(fragment)
//        }

//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }


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

    override fun onDestroy() {
        super.onDestroy()
        menuComponent = null
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


//    private fun changeFragment(newFragment: Fragment){
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.container, newFragment)
//            .commit()
//    }


    fun showMessage(msg: String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onClickGameMap(map: GameMap) {
        val navId = findNavController(R.id.nav_host_fragment).currentDestination?.id

        val mode = when (navId) {
            R.id.nav_solo -> "solo"
            R.id.nav_time -> "time"
            else -> null
        } ?: return
        startGame(map.id, mode)
    }

    private fun startGame(mapId: Int, mode: String) {
        val intent = when (mode) {
            "solo" -> Intent(this, ClassicGameActivity::class.java)
            "time" -> Intent(this, TimeLimitGameActivity::class.java)
            "mp" -> Intent(this, MultiplayerGameActivity::class.java)
            "street" -> Intent(this, StreetGameActivity::class.java)
            else -> null
        } ?: return
        startActivity(intent)
        createGameComponent(mode, mapId)
    }


    private fun createGameComponent(mode: String, mapId: Int) {
        val gameInfo = getGameInfo(mode, mapId)
        val startLocation = getStartLocation()
        (applicationContext as AppDelegate)
            .createGameComponent(gameInfo, startLocation)
    }

    fun showTableOfRecords() {
        val intent = Intent(this, RecordsActivity::class.java)
        startActivity(intent)
    }

    private fun getGameInfo(mode: String, mapId: Int) = GameInfo(mode, mapId, 5)

    private fun getStartLocation() = LatLng(64.0, 80.0)

}