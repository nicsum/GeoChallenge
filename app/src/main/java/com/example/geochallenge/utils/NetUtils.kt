package com.example.geochallenge.utils


import io.reactivex.Single
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

fun getGameId(response: Response): String? {
    val jsonData = response.body?.string() ?: return null
    val jobject = JSONObject(jsonData)
    return jobject.getString("gameId")
}


fun hasInternetConnection(): Single<Boolean> {
    return Single.fromCallable {
        try {
            // Connect to Google DNS to check for connection
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddress = InetSocketAddress("8.8.8.8", 53)

            socket.connect(socketAddress, timeoutMs)
            socket.close()

            true
        } catch (e: IOException) {
            false
        }
    }

}