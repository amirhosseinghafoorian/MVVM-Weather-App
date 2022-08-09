package com.example.mvvmweatherapp.ui.presentation.setting

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.ui.util.Resource
import com.example.mvvmweatherapp.ui.util.Resource.*

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
            // todo show snackbar permissions not granted"
        }
    }

    Scaffold(scaffoldState = rememberScaffoldState()) {
        Column {

            Text("select your location type")

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                when (viewModel.isLocationFromGPS.value) {
                    is Empty, is Success -> {
                        Button(
                            modifier = Modifier.background(
                                if (viewModel.isLocationFromGPS.value.data == true) Color.Green
                                else Color.Red
                            ),
                            onClick = {
                                viewModel.changeLocationType(true)
                                permissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                    )
                                )
                            }
                        ) {
                            Text("GPS")
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Button(
                            modifier = Modifier.background(
                                if (viewModel.isLocationFromGPS.value.data == false) Color.Green
                                else Color.Red
                            ),
                            onClick = {
                                viewModel.changeLocationType(false)
                            }
                        ) {
                            Text("Input city name")
                        }
                    }
                    is Loading -> {
                        LinearProgressIndicator()
                    }
                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.cityLocation.value) {
                is Empty -> {
                    Text("empty")
                }
                is Error -> {
                    Text("Error")
                }
                is Loading -> {
                    Text("Loading")
                }
                is Success -> {
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
        }
    }
}