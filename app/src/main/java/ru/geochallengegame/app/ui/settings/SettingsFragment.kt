package ru.geochallengegame.app.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.ui.menu.MenuActivity
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var settingsManager: SettingsManager


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? MenuActivity)?.supportActionBar?.title = getString(R.string.settings)
        (activity as? MenuActivity)?.supportActionBar?.subtitle = ""
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        settingsManager.setDefaultValues()
//        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false)
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as? AppDelegate)
            ?.appComponent
            ?.inject(this)

    }
}