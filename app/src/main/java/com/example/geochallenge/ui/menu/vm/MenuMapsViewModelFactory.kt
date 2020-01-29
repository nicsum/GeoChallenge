package com.example.geochallenge.ui.menu.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.GeochallengeService
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MenuMapsViewModelFactory @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val geochallengeService: GeochallengeService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(FirebaseAuth::class.java, GeochallengeService::class.java)
            .newInstance(firebaseAuth, geochallengeService)
    }
}