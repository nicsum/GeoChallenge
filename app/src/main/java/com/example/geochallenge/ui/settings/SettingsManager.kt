package com.example.geochallenge.ui.settings

import androidx.preference.PreferenceManager
import com.example.geochallenge.AppDelegate
import java.util.*
import javax.inject.Inject


class SettingsManager @Inject constructor(val appDelegate: AppDelegate) {

    companion object {
        const val RUSSIAN_LANG_CODE = "ru"
        const val ENGLISH_LANG_CODE = "en"
    }


    fun setDefaultValues() {
        val pm = PreferenceManager
            .getDefaultSharedPreferences(appDelegate)
        val lang = pm.getString(SettingsActivity.LANG_TASK_KEY, null)

        if (lang == null) {
            pm.edit()
                .putString(SettingsActivity.LANG_TASK_KEY, getDefaultLang())
                .apply()
        }
    }

    fun setDefaultTaskLang(lang: String) {
        val pm = PreferenceManager
            .getDefaultSharedPreferences(appDelegate)

        pm.edit()
            .putString(SettingsActivity.LANG_TASK_KEY, lang)
            .apply()
    }

    fun getDefaultTaskLang(): String {

        return PreferenceManager
            .getDefaultSharedPreferences(appDelegate)
            .getString(SettingsActivity.LANG_TASK_KEY, null) ?: getDefaultLang()
    }

    private fun getDefaultLang(): String {
        return Locale.getDefault().language
    }
}