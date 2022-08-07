package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.navigation.AppScreens.Setting_ROUTE

@Composable
fun HomeScreen(
    navController: NavController
) {
    HomeScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {
    Scaffold(scaffoldState = rememberScaffoldState()) {
        Column {
            Text("Home")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigate(Setting_ROUTE)
                }
            ) {
                Text("go to setting")
            }
        }
    }
}