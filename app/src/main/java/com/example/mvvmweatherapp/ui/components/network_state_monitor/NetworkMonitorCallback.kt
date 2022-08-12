package com.example.mvvmweatherapp.ui.components.network_state_monitor

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import com.example.mvvmweatherapp.ui.components.InternetNotifier
import javax.inject.Inject

class NetworkMonitorCallback @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val internetNotifier: InternetNotifier
) : NetworkCallback() {

    override fun onLost(network: Network) {
        super.onLost(network)
        internetNotifier.onNetworkStateChanged(NetworkStates.Lost)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        internetNotifier.onNetworkStateChanged(NetworkStates.Available)
    }

    fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(this)
    }

    fun unRegisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(this)
    }
}