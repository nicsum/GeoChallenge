package ru.geochallengegame.app.ui.menu

import android.content.Context
import androidx.fragment.app.Fragment

class SignOutActionFragment : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignOutable) context.signOut()
    }
}