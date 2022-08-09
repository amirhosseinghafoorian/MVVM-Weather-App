package com.example.mvvmweatherapp.ui.components

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SnackbarObserver(
    scaffoldState: ScaffoldState,
    snackbarFlow: SharedFlow<String>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {

    LaunchedEffect(scaffoldState.snackbarHostState) {
        coroutineScope.launch {
            snackbarFlow.collectLatest { message ->
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
        }
    }
}