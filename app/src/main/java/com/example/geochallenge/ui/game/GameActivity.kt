package com.example.geochallenge.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.activity.ActivityComponent
import com.example.geochallenge.di.classic.ClassicGameComponent
import com.example.geochallenge.di.classic.ClassicGameModule
import com.example.geochallenge.di.multiplayer.MultiplayerGameComponent
import com.example.geochallenge.di.multiplayer.MultiplayerGameModule
import com.example.geochallenge.di.time.TimeGameComponent
import com.example.geochallenge.di.time.TimeGameModule
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject


class GameActivity : AppCompatActivity() {

    companion object {

        const val TYPE_GAME_KEY = "TYPE_GAME_KEY"
        const val START_LOCATION_KEY = "START_LOCATION_KEY"
        const val COUNT_TASKS_FOR_LEVEL_KEY = "COUNT_TASKS_FOR_LEVEL_KEY"

        const val DEFAULT_TYPE_GAME = "DEFAULT_TYPE_GAME"
        const val CLASSIC_TYPE_GAME = "CLASSIC_TYPE_GAME"
        const val TIME_LIMIT_TYPE_GAME = "TIME_LIMIT_TYPE_GAME"
        const val MULTIPLAYER_TYPE_GAME = "MULTIPLAYER_TYPE_GAME"
    }

    @Inject
    lateinit var fragment: BaseGameInfoFragment


    var isFirstStartActivity: Boolean = false


    lateinit var mapComponent: MapComponent
    lateinit var typeGame: String
    lateinit var activityComponent: ActivityComponent


    val classicComponent: ClassicGameComponent by lazy {
        val countTasks = getCountTasksForLevel()
        activityComponent.provideClassicGameComponent(ClassicGameModule(countTasks))
    }

    val multiplayerComponent: MultiplayerGameComponent by lazy {
        val countTasks = getCountTasksForLevel()
        activityComponent
            .provideMultiplayerGameComponent(MultiplayerGameModule(countTasks))
    }

    val timeComponent: TimeGameComponent by lazy {
        val countTasks = getCountTasksForLevel()
        activityComponent.provideTimeGameComponent(TimeGameModule(countTasks))
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        isFirstStartActivity = savedInstanceState == null

        val startLocation = getStartLocation()
        typeGame = getGameType()

        activityComponent = getActivityComponent(typeGame, startLocation)
        activityComponent.inject(this)

        mapComponent = when (typeGame) {
            CLASSIC_TYPE_GAME -> classicComponent
            TIME_LIMIT_TYPE_GAME -> timeComponent
            MULTIPLAYER_TYPE_GAME -> multiplayerComponent
            else -> classicComponent
        }

        setContentView(R.layout.ac_game)
        supportFragmentManager.beginTransaction()
            .replace(R.id.game_info_container, fragment)
            .commit()

    }

    override fun onStart() {
        super.onStart()
        getViewModel().isGameFinished.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Игра окончена", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecordsActivity::class.java))
                (application as AppDelegate).dropGameActivityComponent()
                finish()
            }
        })
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is BaseGameInfoFragment) {


        }

    }

    override fun onDestroy() {
        super.onDestroy()
        (application as AppDelegate).dropGameActivityComponent()
    }


    private fun getViewModel(): BaseGameViewModel {
        return (supportFragmentManager
            .findFragmentById(R.id.game_info_container) as BaseGameInfoFragment).viewModel
    }

    override fun onResume() {
        super.onResume()
        if (isFirstStartActivity)
            getViewModel().newGame()
    }

    override fun onPause() {
        super.onPause()
        isFirstStartActivity = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        getViewModel().finishGame()
    }

    private fun getActivityComponent(
        typeGame: String,
        startLocation: LatLng
    ): ActivityComponent {
        return (application as AppDelegate).getGameActivityComponent(this, typeGame, startLocation)

    }

    private fun getStartLocation(): LatLng {
        return intent.extras?.getSerializable(START_LOCATION_KEY).let {
            it as Pair<Double, Double>
            LatLng(it.first, it.second)
        }
    }

    private fun getCountTasksForLevel() = 5
    private fun getGameType() = intent.extras?.getString(TYPE_GAME_KEY) ?: DEFAULT_TYPE_GAME

}