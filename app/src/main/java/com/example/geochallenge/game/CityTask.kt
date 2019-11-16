package com.example.geochallenge.game


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cities")
class CityTask(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int? = 0,
    @ColumnInfo(name = "city_name") var city: String? = "",
    @ColumnInfo(name = "country") var country: String? = "",
    @ColumnInfo(name = "country_ru") var countryRU: String? = "",
    @ColumnInfo(name = "latitude") var latitude: Double? = 0.0,
    @ColumnInfo(name = "longitude") var longitude: Double? = 0.0,
    @ColumnInfo(name = "level") var level: Int? = 0,
    @ColumnInfo(name = "rating") var rating: Int? = 0
)