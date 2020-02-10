package ru.geochallengegame.app.ui.menu.rv

import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.geochallengegame.R
import ru.geochallengegame.app.AppDelegate
import ru.geochallengegame.app.game.GameMap
import ru.geochallengegame.app.ui.menu.OnClickMapListener
import ru.geochallengegame.app.ui.settings.SettingsManager
import java.util.*

class GameMapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var map: GameMap
    private lateinit var langsRadioGroup: RadioGroup
    private lateinit var lang: String

    fun bind(modeWithLeaderboard: Boolean, map: GameMap, listener: OnClickMapListener) {
        try {
            Picasso.with(itemView.context)
                .load(map.imageUrl)
                .placeholder(R.drawable.ic_world)
                .error(R.drawable.ic_world)
                .into(itemView.findViewById<ImageView>(R.id.card_bg_image))
        } catch (e: Exception) {
        }

        val leadersViews = listOf<TextView>(
            itemView.findViewById(R.id.firstPlaceUserTextView),
            itemView.findViewById(R.id.secondPlaceUserTextView),
            itemView.findViewById(R.id.thirdPlaceUserTextView)
        )

        val leaderboardBtn = itemView.findViewById<Button>(R.id.lbButton)

        if (modeWithLeaderboard) {
            itemView.findViewById<TextView>(R.id.firstPlace).visibility = View.VISIBLE
            itemView.findViewById<TextView>(R.id.secondPlace).visibility = View.VISIBLE
            itemView.findViewById<TextView>(R.id.thirdPlace).visibility = View.VISIBLE
            map.leaders?.forEachIndexed { i, leader ->
                leadersViews[i].text = "${leader.username} ( ${leader.score})"
            }
            leaderboardBtn.setOnClickListener {
                listener.onClickLeaderboard(map, lang)
            }

        } else {
            itemView.findViewById<TextView>(R.id.firstPlace).visibility = View.GONE
            itemView.findViewById<TextView>(R.id.secondPlace).visibility = View.GONE
            itemView.findViewById<TextView>(R.id.thirdPlace).visibility = View.GONE
            leaderboardBtn.visibility = View.GONE
            leadersViews.forEach {
                it.visibility = View.GONE
            }
        }

        this.map = map
        val mapTv = itemView.findViewById<TextView>(R.id.mapText)
        langsRadioGroup = itemView.findViewById(R.id.langsRadioGroup)
        val enRadioButton = itemView.findViewById<RadioButton>(R.id.radio_en)
        val ruRadioButton = itemView.findViewById<RadioButton>(R.id.radio_ru)

        mapTv.text = if (Locale.getDefault().language == "ru")
            map.mapRu
        else
            map.mapEn
        itemView.setOnClickListener {
            listener.onClickGameMap(map, lang)
        }

        val sm = (itemView.context.applicationContext as AppDelegate)
            .appComponent
            .getSettingsManager()

        langsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
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