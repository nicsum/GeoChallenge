package com.example.geochallenge.ui.menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.geochallenge.BuildConfig
import com.example.geochallenge.R

class AboutFragment : Fragment() {

    lateinit var versionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fr_about, container, false)
        versionTextView = view.findViewById(R.id.app_build)
        versionTextView.text = BuildConfig.VERSION_NAME
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.about)
        (activity as MenuActivity).supportActionBar?.subtitle = null
    }
}
