package com.example.mvvmweatherapp.ui.components

object BroadCastReceivers {

    val networkChangeReceiver: NetworkChangeReceiver by lazy {
        NetworkChangeReceiver()
    }

    val networkChangeReceiverManager : NetworkChangeReceiverManager by lazy {
        NetworkChangeReceiverManager()
    }

}