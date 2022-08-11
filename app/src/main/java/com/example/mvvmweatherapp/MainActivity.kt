package com.example.mvvmweatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.mvvmweatherapp.ui.components.BroadCastReceivers
import com.example.mvvmweatherapp.ui.components.NetworkChangeReceiverManager
import com.example.mvvmweatherapp.ui.theme.MVVMWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var networkChangeReceiverManager: NetworkChangeReceiverManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // todo connectivity manager by philipp
        } else {
            networkChangeReceiverManager = BroadCastReceivers.networkChangeReceiverManager
        }

        networkChangeReceiverManager?.registerReceiver(this)

        setContent {
            AppScreen()
        }
    }

    override fun onDestroy() {
        networkChangeReceiverManager?.unregisterReceiver(this)
        super.onDestroy()
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MVVMWeatherAppTheme {
        Greeting("Android")
    }
}