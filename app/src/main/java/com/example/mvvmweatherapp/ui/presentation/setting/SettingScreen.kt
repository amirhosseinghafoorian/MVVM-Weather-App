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
import androidx.navigation.NavController

@Composable
fun SettingScreen(
    navController: NavController
) {
    Scaffold(scaffoldState = rememberScaffoldState()) {
        Column {
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