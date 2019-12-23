package com.example.geochallenge.net

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetworkChangeReceiver : BroadcastReceiver() {

    var listener: NetworkIsChangeListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {


        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent?.action) {
            try {
                val cm =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = cm.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
                    listener?.onChange(true)
                } else {
                    listener?.onChange(false)
                }
            } catch (e: Exception) {
            }

//            if(status == NetworkUtils.NETWORK_STATUS_NOT_CONNECTED){
//                listener?.onChange(false)
//            }else{
//                listener?.onChange(true)
//            }
        }
    }

    fun bind(listener: NetworkIsChangeListener) {
        this.listener = listener
    }

    fun unbind() {
        listener = null
    }
}