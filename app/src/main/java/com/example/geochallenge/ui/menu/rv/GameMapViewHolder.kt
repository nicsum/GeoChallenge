package com.example.geochallenge.ui.menu.rv

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.game.GameMap
import com.example.geochallenge.ui.menu.OnClickMapListener

class GameMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var map: GameMap
    lateinit var langsRadioGroup: RadioGroup
    var lang: String? = null

    fun bind(map: GameMap, listener: OnClickMapListener) {
        this.map = map
        val mapTv = itemView.findViewById<TextView>(R.id.mapText)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            mapTv,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        )
        langsRadioGroup = itemView.findViewById(R.id.langsRadioGroup)
        val enRadioButton = itemView.findViewById<RadioButton>(R.id.radio_en)
        val ruRadioButton = itemView.findViewById<RadioButton>(R.id.radio_ru)

        ruRadioButton.isVisible = map.langRu
        enRadioButton.isVisible = map.langEn

        langsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            lang = when (checkedId) {
                R.id.radio_en -> "en"
                R.id.radio_ru -> "ru"
                else -> "ru" //TODO
            }
        }
        ruRadioButton.isChecked = true
        mapTv.text = map.mapRu
        itemView.setOnClickListener {
            listener.onClickGameMap(map, lang ?: "ru")
        }
    }
}