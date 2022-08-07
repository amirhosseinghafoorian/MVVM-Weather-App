package com.example.mvvmweatherapp

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.mvvmweatherapp.navigation.AppNavGraph
import com.example.mvvmweatherapp.ui.theme.MVVMWeatherAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppScreen() {
    MVVMWeatherAppTheme {

        val systemUiController = rememberSystemUiController()
        val backgroundColor = MaterialTheme.colors.background
        val isLight = MaterialTheme.colors.isLight

        SideEffect {
            systemUiController.setSystemBarsColor(backgroundColor, isLight)
        }

        AppNavGraph()
    }
}