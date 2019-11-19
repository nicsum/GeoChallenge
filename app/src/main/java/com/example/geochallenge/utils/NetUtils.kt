package com.example.geochallenge.utils


import okhttp3.Response
import org.json.JSONObject

fun getGameId(response: Response): String? {
    val jsonData = response.body?.string() ?: return null
    val jobject = JSONObject(jsonData)
    return jobject.getString("gameId")
}

fun getCoordinates(response: Response): Pair<Double, Double>? {
    val jsonData = response.body?.string() ?: return null
    val jobject = JSONObject(jsonData)
    val coordinateString = jobject.getString("geoPoint")
    return coordinateString
        .removePrefix("[")
        .removePrefix("]")
        .replace(" ", "")
        .split(",")
        .map { it.toDouble() }
        .let {
            Pair(it[0], it[1])
        }
}