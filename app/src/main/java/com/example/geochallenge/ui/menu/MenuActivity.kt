package com.example.geochallenge.ui.menu


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.menu.MenuComponent
import com.example.geochallenge.game.GameInfo
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.game.classic.ClassicGameActivity
import com.example.geochallenge.ui.game.time.TimeLimitGameActivity
import com.example.geochallenge.ui.menu.vm.MenuMapsViewModel
import com.example.geochallenge.ui.splash.SplashActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MenuActivity : AppCompatActivity(), OnClickMapListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var gameMapIsSelected = false

    @Inject
    lateinit var viewModel: MenuMapsViewModel

    var menuComponent: MenuComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        menuComponent = (applicationContext as AppDelegate)
            .userComponent
            ?.menuComponent()
            ?.create(this)

        menuComponent?.inject(this)


        super.onCreate(savedInstanceState)


        setContentView(R.layout.ac_menu)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val coordinator: CoordinatorLayout = findViewById(R.id.coordinator)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        refreshLayout = findViewById(R.id.refreshLayout)

        refreshLayout.setOnRefreshListener {
            viewModel.loadMaps()
        }
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

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_solo, R.id.nav_time, R.id.nav_settings), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val actionBarDrawerToggle: ActionBarDrawerToggle = object :
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.app_name,
                R.string.app_name
            ) {
            private val scaleFactor = 6f
            override fun onDrawerSlide(
                drawerView: View,
                slideOffset: Float
            ) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset

                coordinator.translationX = slideX
                coordinator.scaleX = 1 - (slideOffset / scaleFactor)
                coordinator.scaleY = 1 - (slideOffset / scaleFactor)
                //                coordinator.rotationY = slideOffset * -90f
                coordinator.rotationY = slideOffset * -20f
                //                coordinator.rotationY = 0 - slideOffset *5
            }
        }

        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.drawerElevation = 0f
        drawerLayout.addDrawerListener(actionBarDrawerToggle)


    }

    override fun onResume() {
        super.onResume()
        gameMapIsSelected = false

        viewModel.loadingIsVisible.observe(
            this,
            Observer {
                refreshLayout.isRefreshing = it
            })

        viewModel.errorIsVisible.observe(
            this,
            Observer {
                if (it) Snackbar
                    .make(
                        findViewById(R.id.drawer_layout),
                        R.string.menu_error,
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }
        )

        viewModel.isSignOut.observe(
            this,
            Observer {
                if (it)
                    signOut()
            }
        )


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(FirebaseAuth.getInstance().currentUser !=null)
            menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            viewModel.logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        menuComponent = null
    }

    private fun signOut() {
        (application as AppDelegate).destroyUserComponent()
        splash()
        finish()
    }


    private fun splash() {
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

    override fun onClickGameMap(map: GameMap, lang: String) {
        if (gameMapIsSelected) return

        gameMapIsSelected = true

        val mode = when (findNavController(R.id.nav_host_fragment).currentDestination?.id) {
            R.id.nav_solo -> "solo"
            R.id.nav_time -> "time"
            else -> null
        } ?: return
        startGame(map, mode, lang)
    }

    private fun startGame(map: GameMap, mode: String, lang: String) {
        val intent = when (mode) {
            "solo" -> Intent(this, ClassicGameActivity::class.java)
            "time" -> Intent(this, TimeLimitGameActivity::class.java)
            else -> null
        } ?: return
        startActivity(intent)
        createGameComponent(mode, map, lang)
    }


    private fun createGameComponent(mode: String, map: GameMap, lang: String) {
        val gameInfo = getGameInfo(mode, map.id, lang)
        (applicationContext as AppDelegate)
            .createGameComponent(gameInfo, map)
    }


    private fun getGameInfo(mode: String, mapId: Int, lang: String) = GameInfo(mode, mapId, 5, lang)


}