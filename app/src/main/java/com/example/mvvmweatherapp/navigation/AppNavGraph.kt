package com.example.mvvmweatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvmweatherapp.navigation.AppScreens.HOME_ROUTE
import com.example.mvvmweatherapp.navigation.AppScreens.Setting_ROUTE
import com.example.mvvmweatherapp.ui.presentation.home.HomeScreen
import com.example.mvvmweatherapp.ui.presentation.setting.SettingScreen

@Composable
    fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = modifier
    ) {
        composable(route = HOME_ROUTE) {
            HomeScreen(navController)
        }
        composable(route = Setting_ROUTE) {
            SettingScreen(navController)
        }
    }
}