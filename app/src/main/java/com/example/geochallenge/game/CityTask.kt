package com.example.geochallenge.game


import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName


class CityTask(
    @NonNull
    var id: Int? = 0,

    @SerializedName("name")
    var name: String? = "",

    var country: String? = "",

    var latitude: Double? = 0.0,
    var longitude: Double? = 0.0,
    var level: Int? = 0
)