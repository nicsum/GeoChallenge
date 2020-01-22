package com.example.geochallenge.ui.menu.rv

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.AppDelegate
import com.example.geochallenge.R
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.menu.OnClickMapListener
import com.example.geochallenge.ui.settings.SettingsManager

class GameMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var map: GameMap
    lateinit var langsRadioGroup: RadioGroup

    lateinit var lang: String

    fun bind(map: GameMap, listener: OnClickMapListener) {
        this.map = map

        val mapTv = itemView.findViewById<TextView>(R.id.mapText)
        langsRadioGroup = itemView.findViewById(R.id.langsRadioGroup)
        val enRadioButton = itemView.findViewById<RadioButton>(R.id.radio_en)
        val ruRadioButton = itemView.findViewById<RadioButton>(R.id.radio_ru)

        mapTv.text = map.mapRu
        itemView.setOnClickListener {
            listener.onClickGameMap(map, lang)
        }

        val sm = (itemView.context.applicationContext as AppDelegate)
            .appComponent
            .getSettingsManager()

        langsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            lang = when (checkedId) {
                R.id.radio_en -> SettingsManager.ENGLISH_LANG_CODE
                R.id.radio_ru -> SettingsManager.RUSSIAN_LANG_CODE
                else -> throw IllegalArgumentException("no such lang code")
            }
        }

        val defaultLang = sm.getDefaultTaskLang()

        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            mapTv,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )

        ruRadioButton.isVisible = map.langRu
        enRadioButton.isVisible = map.langEn
//        val enIsDefault = defaultLang == SettingsManager.ENGLISH_LANG_CODE
        val ruIsDefault = defaultLang == SettingsManager.RUSSIAN_LANG_CODE
        if (ruIsDefault) {
            if (!map.langRu) {
                ruRadioButton.isChecked = false
                enRadioButton.isChecked = true
            } else {
                ruRadioButton.isChecked = true
                enRadioButton.isChecked = false
            }
        } else {
            if (!map.langEn) {
                ruRadioButton.isChecked = true
                enRadioButton.isChecked = false
            } else {
                ruRadioButton.isChecked = false
                enRadioButton.isChecked = true
            }
        }
    }

}