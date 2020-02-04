package ru.geochallengegame.app.ui.menu


import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.di.menu.MenuComponent
import ru.geochallengegame.app.game.GameInfo
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.ui.game.classic.ClassicGameActivity
import ru.geochallengegame.app.ui.game.hundred.HungredGameActivity
import ru.geochallengegame.app.ui.game.immortal.ImmortalGameActivity
import ru.geochallengegame.app.ui.game.time.TimeLimitGameActivity
import ru.geochallengegame.app.ui.menu.vm.MenuMapsViewModel
import ru.geochallengegame.app.ui.splash.SplashActivity
import javax.inject.Inject

class MenuActivity : AppCompatActivity(),
    OnClickMapListener {

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

        findNavController(R.id.nav_host_fragment)
            .addOnDestinationChangedListener { _, destination, _ ->
                viewModel.selectMode(getCurrentMode(destination))
            }


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_solo,
                R.id.nav_time,
                R.id.nav_immortal,
                R.id.nav_hungred,
                R.id.nav_settings,
                R.id.nav_about
            ), drawerLayout
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
        viewModel.gameInfoIsVisible.observe(
            this,
            Observer {
                if (it) showInfoDialog()
            })
    }

    override fun onRestart() {
        super.onRestart()
        gameMapIsSelected = false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null)
            menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            viewModel.logout()
            return true
        }
        if (item.itemId == R.id.modeInfo) {
            viewModel.info()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun showInfoDialog() {
        MessageDialog()
            .show(supportFragmentManager, "MessageDialog")

    }

    override fun onPause() {
        super.onPause()
        val dialog = supportFragmentManager
            .findFragmentByTag("MessageDialog") as? MessageDialog ?: return
        supportFragmentManager.beginTransaction().remove(dialog).commit()

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


    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onClickGameMap(map: GameMap, lang: String) {
        if (gameMapIsSelected) return

        gameMapIsSelected = true

        val mode = getCurrentMode() ?: return
        startGame(map, mode, lang)
    }

    private fun startGame(map: GameMap, mode: String, lang: String) {
        val intent = when (mode) {
            "solo" -> Intent(this, ClassicGameActivity::class.java)
            "time" -> Intent(this, TimeLimitGameActivity::class.java)
            "endless" -> Intent(this, ImmortalGameActivity::class.java) //TODO rename
            "fatal100" -> Intent(this, HungredGameActivity::class.java)
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

    private fun getCurrentMode(): String? {
        return getCurrentMode(findNavController(R.id.nav_host_fragment).currentDestination)
    }

    private fun getCurrentMode(distenation: NavDestination?): String? {
        return when (distenation?.id) {
            R.id.nav_solo -> "solo"
            R.id.nav_time -> "time"
            R.id.nav_immortal -> "endless" //TODO rename
            R.id.nav_hungred -> "fatal100"
            else -> null
        }
    }


    private fun getGameInfo(mode: String, mapId: Int, lang: String) = GameInfo(mode, mapId, 5, lang)


    class MessageDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val context =
                (activity as? MenuActivity) ?: return super.onCreateDialog(savedInstanceState)
            val mode = context.getCurrentMode()
            val message = getInfoMessage(mode)
            return AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    context.viewModel.iReadModeInfo()
                }
                .create()
        }

        override fun onCancel(dialog: DialogInterface) {
            super.onCancel(dialog)
            (context as MenuActivity).viewModel.iReadModeInfo()
        }

        private fun getInfoMessage(mode: String?): String? {
            //TODO
            return when (mode) {
                "solo" -> "waefgrg"
                "time" -> "ddd"
                else -> null
            }
        }
    }

}