package com.example.mvvmweatherapp.ui.presentation.setting

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMapResult ->

        val fineLocationAccessResult: Boolean? =
            permissionsMapResult[Manifest.permission.ACCESS_FINE_LOCATION]
        val coarseLocationAccessResult: Boolean? =
            permissionsMapResult[Manifest.permission.ACCESS_COARSE_LOCATION]

        val isAllPermissionsGranted = fineLocationAccessResult != null &&
                coarseLocationAccessResult != null &&
                fineLocationAccessResult == true &&
                fineLocationAccessResult == true

        if (isAllPermissionsGranted) {
            viewModel.getCurrentLocation()
        } else {
            // todo permissions not granted"
        }
    }

    Scaffold(scaffoldState = rememberScaffoldState()) {
        Column {
            when (viewModel.cityName.value) {
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
                    Text(viewModel.cityName.value.data.toString())
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.cityLocation.value) {
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
                    viewModel.cityLocation.value.data?.let {
                        Text("lat : ${it.first}, lon : ${it.second}")
                    }
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

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.getCityLocation("London")
                }
            ) {
                Text("get London Location")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.getCityName()
                }
            ) {
                Text("get City name")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    )
                }
            ) {
                Text("get current location")
            }
        }
    }
}