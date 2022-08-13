package com.example.mvvmweatherapp

import android.content.BroadcastReceiver
import android.os.Build
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.mvvmweatherapp.ui.components.UpdateWeatherWorker
import com.example.mvvmweatherapp.ui.components.network_broadcast_receiver.NetworkChangeReceiver
import com.example.mvvmweatherapp.ui.components.network_state_monitor.NetworkMonitorCallback
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkMonitorCallback: NetworkMonitorCallback,
    private val networkChangeReceiver: NetworkChangeReceiver,
    private val workManager: WorkManager
) : BaseViewModel() {

    private var isNetworkMonitoringHandled = false

    private val _registerReceiverSharedFlow =
        MutableSharedFlow<Pair<BroadcastReceiver, Boolean>>()
    val registerReceiverSharedFlow =
        _registerReceiverSharedFlow.asSharedFlow()

    init {
        executeWorker()
    }

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

    private fun executeWorker() {
        val request = PeriodicWorkRequest.Builder(
            UpdateWeatherWorker::class.java,
            30,
            TimeUnit.MINUTES
        )
            .setConstraints(
                Constraints
                    .Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setInitialDelay(
                30,
                TimeUnit.MINUTES
            )
            .build()
        workManager.enqueue(request)
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