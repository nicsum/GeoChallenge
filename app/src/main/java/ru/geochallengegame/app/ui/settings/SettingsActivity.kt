package ru.geochallengegame.app.ui.settings

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import ru.geochallengegame.R


class SettingsActivity : AppCompatActivity() {

    companion object {
        const val LANG_TASK_KEY = "lang_task_key"
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.ac_settings)
    }


}