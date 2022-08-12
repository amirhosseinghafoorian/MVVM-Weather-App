package com.example.mvvmweatherapp

import android.content.BroadcastReceiver
import android.os.Build
import androidx.lifecycle.viewModelScope
import com.example.mvvmweatherapp.ui.components.network_broadcast_receiver.NetworkChangeReceiver
import com.example.mvvmweatherapp.ui.components.network_state_monitor.NetworkMonitorCallback
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkMonitorCallback: NetworkMonitorCallback,
    private val networkChangeReceiver: NetworkChangeReceiver
) : BaseViewModel() {

    private var isNetworkMonitoringHandled = false

    private val _registerReceiverSharedFlow =
        MutableSharedFlow<Pair<BroadcastReceiver, Boolean>>()
    val registerReceiverSharedFlow =
        _registerReceiverSharedFlow.asSharedFlow()

    fun setupNetworkMonitoring() {
        if (!isNetworkMonitoringHandled) {
            isNetworkMonitoringHandled = true

            viewModelScope.launch {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    networkMonitorCallback.registerNetworkCallback()
                } else {
                    _registerReceiverSharedFlow.emit(
                        Pair(
                            networkChangeReceiver,
                            true
                        )
                    )
                }
            }
        }
    }

    override fun onCleared() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkMonitorCallback.unRegisterNetworkCallback()
        } else {
            viewModelScope.launch {
                _registerReceiverSharedFlow.emit(
                    Pair(
                        networkChangeReceiver,
                        false
                    )
                )
            }
        }

        super.onCleared()
    }


}