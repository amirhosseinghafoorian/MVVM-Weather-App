package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mvvmweatherapp.navigation.AppScreens.Setting_ROUTE
import com.example.mvvmweatherapp.ui.util.Resource.*

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (viewModel.hasSavedLatAndLon.value) {
                is Error -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.Red.copy(alpha = 0.5f))
                            .border(
                                width = 1.dp,
                                color = Color.Red,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                navController.navigate(Setting_ROUTE)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select location")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
                else -> {}
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.Blue.copy(alpha = 0.5f))
                    .border(
                        width = 1.dp,
                        color = Color.Blue,
                        shape = MaterialTheme.shapes.medium
                    )
                    .heightIn(min = 128.dp)
                    .clickable {
                        navController.navigate(Setting_ROUTE)
                    }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (viewModel.currentDayForecast.value) {
                    is Success -> {
                        Text(viewModel.currentDayForecast.value.data!![0].cityName)

                        Spacer(modifier = Modifier.height(64.dp))

                        Text(viewModel.currentDayForecast.value.data!![0].temperature + " °C")

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(viewModel.currentDayForecast.value.data!![0].description)
                    }
                    is Empty -> {
                        Text("No Location selected")
                    }
                    else -> {}
                }
            }

            when (viewModel.threeDayForecast.value) {
                is Success -> {

                    viewModel.threeDayForecast.value.data!!.forEach { singleDayForecast ->

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .background(Color.Blue.copy(alpha = 0.5f))
                                .border(
                                    width = 1.dp,
                                    color = Color.Blue,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(16.dp)
                        ) {
                            Text(singleDayForecast.date)

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(singleDayForecast.description)

                            Spacer(modifier = Modifier.weight(1f))

                            Text(singleDayForecast.temperature + " °C")
                        }
                    }
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // todo weather ui
        }
    }
}