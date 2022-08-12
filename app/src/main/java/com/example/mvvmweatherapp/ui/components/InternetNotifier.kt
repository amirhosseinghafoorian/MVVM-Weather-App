package com.example.mvvmweatherapp.ui.components

import com.example.mvvmweatherapp.ui.components.network_state_monitor.NetworkStates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class InternetNotifier {

    private val _notifyInternetSharedFlow =
        MutableSharedFlow<NetworkStates>()
    val notifyInternetSharedFlow =
        _notifyInternetSharedFlow.asSharedFlow()

    fun onNetworkStateChanged(networkStates: NetworkStates) {
        CoroutineScope(Dispatchers.IO).launch {
            _notifyInternetSharedFlow.emit(networkStates)
        }
    }

}