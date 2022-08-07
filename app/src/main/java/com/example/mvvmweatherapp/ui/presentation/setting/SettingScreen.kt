package com.example.mvvmweatherapp.ui.presentation.setting

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
import com.example.mvvmweatherapp.ui.util.Resource

@Composable
fun SettingScreen(
    navController: NavController
) {
    SettingScreen(
        navController = navController,
        viewModel = hiltViewModel()
    )
}

@Composable
fun SettingScreen(
    navController: NavController,
    viewModel: SettingViewModel
) {
    Scaffold(scaffoldState = rememberScaffoldState()) {
        Column {
            when (viewModel.state.value) {
                is Resource.Empty -> {
                    Text("empty")
                }
                is Resource.Error -> {
                    Text("Error")
                }
                is Resource.Loading -> {
                    Text("Loading")
                }
                is Resource.Success -> {
                    Text("Success")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Setting")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Text("back to home")
            }
        }
    }
}