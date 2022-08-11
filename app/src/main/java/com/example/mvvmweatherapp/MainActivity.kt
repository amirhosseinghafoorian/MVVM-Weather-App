package com.example.mvvmweatherapp

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.mvvmweatherapp.ui.theme.MVVMWeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.registerReceiverSharedFlow.collect { result ->
                if (result.second) {
                    registerReceiver(
                        result.first,
                        IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                    )
                } else {
                    unregisterReceiver(result.first)
                }
            }
        }

        viewModel.setupNetworkMonitoring()

        setContent {
            AppScreen()
        }
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