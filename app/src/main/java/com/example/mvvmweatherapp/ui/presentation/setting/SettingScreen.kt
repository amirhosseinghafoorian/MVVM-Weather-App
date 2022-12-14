package com.example.mvvmweatherapp.ui.presentation.setting

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.R
import com.example.mvvmweatherapp.ui.components.AppButton
import com.example.mvvmweatherapp.ui.components.AppScaffold
import com.example.mvvmweatherapp.ui.components.AppTextField
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
            viewModel.showSnackbar("location permission denied")
        }
    }

    val scaffoldState = rememberScaffoldState()
    var textFieldValue by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    SnackbarObserver(
        scaffoldState = scaffoldState,
        snackbarFlow = viewModel.snackbarFlow
    )

    AppScaffold(
        scaffoldState = scaffoldState,
        topBarPageName = stringResource(R.string.label_setting),
        topBarLeadingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        navController.navigateUp()
                    }
                    .padding(16.dp),
                tint = Color.White
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.label_select_location_type),
                style = MaterialTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                when (viewModel.isLocationFromGPS.collectAsState().value) {
                    is Empty, is Success -> {
                        AppButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = if (viewModel.isLocationFromGPS.collectAsState().value.data == true) {
                                MaterialTheme.colors.secondary
                            } else {
                                MaterialTheme.colors.primary
                            },
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
                            Text(stringResource(R.string.label_gps))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        AppButton(
                            modifier = Modifier.weight(1f),
                            backgroundColor = if (viewModel.isLocationFromGPS.collectAsState().value.data == false) {
                                MaterialTheme.colors.secondary
                            } else {
                                MaterialTheme.colors.primary
                            },
                            onClick = {
                                viewModel.changeLocationType(false)
                            },
                        ) {
                            Text(stringResource(R.string.label_input_city_name))
                        }
                    }
                    is Loading -> {
                        LinearProgressIndicator()
                    }
                    else -> {}
                }
            }

            when (viewModel.cityName.collectAsState().value) {
                is Success -> {
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colors.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .heightIn(min = 64.dp)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.label_your_selected_city),
                            style = MaterialTheme.typography.subtitle1
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = viewModel.cityName.value.data!!,
                            style = MaterialTheme.typography.h2.copy(
                                color = MaterialTheme.colors.secondary
                            ),
                        )
                    }
                }
                is Loading -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.label_please_wait),
                            style = MaterialTheme.typography.subtitle1.copy(
                                color = MaterialTheme.colors.primary
                            )
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (viewModel.isLocationFromGPS.collectAsState().value.data == false) {
                AppTextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                    },
                    placeholder = stringResource(R.string.label_enter_city_name)
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.getCityLocation(textFieldValue)
                        textFieldValue = ""
                        keyboardController?.hide()
                    }
                ) {
                    Text(stringResource(R.string.label_confirm))
                }
            }
        }
    }
}