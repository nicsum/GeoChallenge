package com.example.geochallenge.ui.menu.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.geochallenge.data.GeochallengeService
import javax.inject.Inject

class MenuMapsViewModelFactory @Inject constructor(
    val geochallengeService: GeochallengeService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass
            .getConstructor(GeochallengeService::class.java)
            .newInstance(geochallengeService)
    }
}