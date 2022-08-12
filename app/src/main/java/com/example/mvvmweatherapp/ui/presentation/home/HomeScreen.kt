package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
            when (viewModel.hasSavedLatAndLon.value) {
                is Resource.Error -> {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clip(MaterialTheme.shapes.medium.copy(CornerSize(16.dp)))
                            .background(Color.Red.copy(alpha = 0.5f))
                            .border(
                                width = 1.dp,
                                color = Color.Red,
                                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
                            )
                            .requiredHeight(64.dp)
                            .clickable {
                                navController.navigate(Setting_ROUTE)
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select location")
                    }
                }
                else -> {}
            }

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

            // todo weather ui
        }
    }
}