package com.example.geochallenge.ui.settings

import androidx.preference.PreferenceManager
import com.example.geochallenge.AppDelegate
import javax.inject.Inject


class SettingsManager @Inject constructor(val appDelegate: AppDelegate) {

    companion object {

        const val RUSSIAN_LANG = "1"
        const val ENGLISH_LANG = "2"
    }

    fun getTaskLang(): String? {
        return PreferenceManager
            .getDefaultSharedPreferences(appDelegate)
            .getString(SettingsActivity.LANG_TASK_KEY, null)
    }
}