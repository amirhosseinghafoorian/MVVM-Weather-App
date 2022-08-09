package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.navigation.AppScreens.Setting_ROUTE
import com.example.mvvmweatherapp.ui.util.Resource

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

            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.cityName.value) {
                is Resource.Success -> {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("city name is : ")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(viewModel.cityName.value.data!!)
                    }
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                else -> {}
            }
        }
    }
}