package com.example.geochallenge.game

import com.google.android.gms.maps.model.LatLng

data class TaskAnswer(
    val answer: LatLng,
    val task: CityTask,
    val playersAnswers: HashMap<String, LatLng?>? = null
)

