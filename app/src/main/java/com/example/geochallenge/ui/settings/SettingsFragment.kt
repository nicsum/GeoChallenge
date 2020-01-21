package com.example.geochallenge.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.ui.menu.MenuActivity
import javax.inject.Inject


class SettingsFragment : PreferenceFragmentCompat() {
    @Inject
    lateinit var settingsManager: SettingsManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.applicationContext as? AppDelegate)
            ?.appComponent
            ?.inject(this)

        (activity as? MenuActivity)?.supportActionBar?.title = getString(R.string.settings_title)
        (activity as? MenuActivity)?.supportActionBar?.subtitle = ""
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        settingsManager.setDefaultValues()
//        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false)
        setPreferencesFromResource(R.xml.preferences, rootKey)

    }
}