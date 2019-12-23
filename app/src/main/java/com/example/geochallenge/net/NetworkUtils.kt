package com.example.geochallenge.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log


class NetworkUtils {


    companion object {
//
//        const val NETWORK_STATUS_WIFI = 1
//        const val NETWORK_STATUS_MOBILE = 2
//        const val NETWORK_STATUS_NOT_CONNECTED = 0


        @JvmStatic
        fun networkIsAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = cm.activeNetwork ?: return false

                val actNw = cm.getNetworkCapabilities(nw) ?: return false
                Log.i("NetworkUtils", actNw.signalStrength.toString())
                Log.i("NetworkUtils", actNw.toString())

                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null) {
                    return when (networkInfo.type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
                return false
            }

        }


    }

}