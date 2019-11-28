package com.example.geochallenge.ui.game

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.di.MapComponent
import com.example.geochallenge.di.activity.ActivityComponent
import com.example.geochallenge.ui.records.RecordsActivity
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject


abstract class BaseGameMapActivity : AppCompatActivity() {

    companion object {
        const val START_LOCATION_KEY = "START_LOCATION_KEY"
        const val COUNT_TASKS_FOR_LEVEL_KEY = "COUNT_TASKS_FOR_LEVEL_KEY"
    }

    @Inject
    lateinit var fragment: BaseGameInfoFragment


    var isFirstStartActivity: Boolean = false

    lateinit var activityComponent: ActivityComponent


    abstract fun getMapComponent(): MapComponent
    abstract fun getLayout(): Int
    abstract fun getViewModel(): BaseGameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isFirstStartActivity = savedInstanceState == null

        val startLocation = getStartLocation()

        activityComponent = getActivityComponent(startLocation)
        activityComponent.inject(this)
        setContentView(getLayout())
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


    override fun onDestroy() {
        super.onDestroy()
        (application as AppDelegate).dropGameActivityComponent()
    }


//    private fun getViewModel(): BaseGameViewModel {
//        return (supportFragmentManager
//            .findFragmentById(R.id.game_info_container) as BaseGameInfoFragment).viewModel
//    }

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
        startLocation: LatLng
    ): ActivityComponent {
        return (application as AppDelegate).getGameActivityComponent(this, startLocation)

    }

    private fun getStartLocation(): LatLng {
        return intent.extras?.getSerializable(START_LOCATION_KEY).let {
            it as Pair<Double, Double>
            LatLng(it.first, it.second)
        }
    }

    protected fun getCountTasksForLevel() = 5

}