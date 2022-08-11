package com.example.mvvmweatherapp.ui.components.network_broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            p0?.let { context ->
                if (isOnline(context)){
                    Log.i("baby", "changed to online")
                } else {
                    Log.i("baby", "changed to offline")
                }
            }
            // todo do what ever you want
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }


}