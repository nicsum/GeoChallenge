package com.example.geochallenge.game

import com.google.gson.annotations.SerializedName

class GameMap(

    @SerializedName("id") var id: Int,
    @SerializedName("map_en") var mapEn: String,
    @SerializedName("map_ru") var mapRu: String,
    @SerializedName("map_name") var name: String,
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("longitude") var longitude: Double?

)