package com.example.geochallenge.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.geochallenge.R

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MenuActivity).supportActionBar?.title = "Настройки"
        (activity as MenuActivity).supportActionBar?.subtitle = ""
        return inflater.inflate(R.layout.fr_settings, container, false)
    }
}