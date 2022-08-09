package com.example.mvvmweatherapp.ui.presentation.setting

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.ui.components.SnackbarObserver
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

@OptIn(ExperimentalComposeUiApi::class)
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

    val scaffoldState = rememberScaffoldState()
    var textFieldValue by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current // todo maybe change

    SnackbarObserver(
        scaffoldState = scaffoldState,
        snackbarFlow = viewModel.snackbarFlow
    )

    Scaffold(scaffoldState = scaffoldState) {
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

            when (viewModel.cityName.value) {
                is Success -> {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("city name is : ")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(viewModel.cityName.value.data!!)
                    }
                }
                is Loading -> {
                    CircularProgressIndicator()
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isLocationFromGPS.value.data == false) {
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.getCityLocation(textFieldValue)
                        textFieldValue = ""
                        keyboardController?.hide()
                    }
                ) {
                    Text("confirm")
                }
            }
        }
    }
}