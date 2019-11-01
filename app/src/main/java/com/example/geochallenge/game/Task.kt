package com.example.geochallenge.game


import com.google.firebase.database.PropertyName

data class Task(@PropertyName("city") var city:String? = "",
                @PropertyName("Lat") var Lat: Double? = 0.0,
                @PropertyName("lng") var lng: Double? = 0.0,
                @PropertyName("level") var level: Int? = 0
                )