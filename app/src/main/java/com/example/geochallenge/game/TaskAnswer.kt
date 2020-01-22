package com.example.geochallenge.game

import com.google.android.gms.maps.model.LatLng

data class TaskAnswer(
    val task: CityTask,
    val answer: LatLng? = null,
    val playersAnswers: HashMap<String, LatLng?>? = null
)

