package com.example.geochallenge.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.geochallenge.R
import com.example.geochallenge.ui.menu.MenuActivity


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as? MenuActivity)?.supportActionBar?.title = "Настройки"
        (activity as? MenuActivity)?.supportActionBar?.subtitle = ""
    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false)
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}