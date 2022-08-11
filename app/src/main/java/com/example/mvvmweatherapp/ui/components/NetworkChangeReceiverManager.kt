package com.example.mvvmweatherapp.ui.components

import android.app.Activity
import android.content.IntentFilter
import android.net.ConnectivityManager

class NetworkChangeReceiverManager {

    fun registerReceiver(activity: Activity) {
        activity.registerReceiver(
            BroadCastReceivers.networkChangeReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    fun unregisterReceiver(activity: Activity) {
        activity.unregisterReceiver(BroadCastReceivers.networkChangeReceiver)
    }

}