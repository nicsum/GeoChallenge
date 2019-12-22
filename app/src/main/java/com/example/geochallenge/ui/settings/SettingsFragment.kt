package com.example.geochallenge.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.geochallenge.R


class SettingsFragment : PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        PreferenceManager.setDefaultValues(activity, R.xml.preferences, false)
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}