package com.example.geochallenge.game

import com.google.gson.annotations.SerializedName

class GameMap(

    @SerializedName("id") var id: Int,
    @SerializedName("map_en") var mapEn: String,
    @SerializedName("map_ru") var mapRu: String,
    @SerializedName("map_name") var name: String,
    @SerializedName("latitude") var latitude: Double?,
    @SerializedName("longitude") var longitude: Double?,
    @SerializedName("lang_ru") var langRu: Boolean,
    @SerializedName("lang_en") var langEn: Boolean,
    @SerializedName("zoom") var zoom: Double?,
    @SerializedName("distance") var distance: Double?,
    @SerializedName("image") var imageUrl: String?

)