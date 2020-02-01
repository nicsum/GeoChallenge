package ru.geochallengegame.app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fr_about.*
import ru.geochallengegame.BuildConfig
import ru.geochallengegame.R

class AboutFragment : Fragment() {

    lateinit var versionTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_about, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app_build.text = BuildConfig.VERSION_NAME
        (activity as MenuActivity).supportActionBar?.title = getString(R.string.about)
        (activity as MenuActivity).supportActionBar?.subtitle = null
    }
}
