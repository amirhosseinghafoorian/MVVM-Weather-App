package com.example.mvvmweatherapp.ui.components.network_state_monitor

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NetworkMonitorCallback @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : NetworkCallback() {

    private val _networkState = MutableStateFlow(NetworkStates.NotSet)
    val networkState = _networkState.asStateFlow()

    override fun onLost(network: Network) {
        super.onLost(network)
        _networkState.value = NetworkStates.Lost
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        _networkState.value = NetworkStates.Available
    }

    fun registerNetworkCallback() {
        connectivityManager.registerDefaultNetworkCallback(this)

    }

    fun unRegisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(this)
    }
}