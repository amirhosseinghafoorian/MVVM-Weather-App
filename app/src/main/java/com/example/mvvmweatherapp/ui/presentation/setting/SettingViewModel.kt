package com.example.mvvmweatherapp.ui.presentation.setting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import com.example.mvvmweatherapp.ui.util.Resource.Empty
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor() : BaseViewModel() {

    private val _state = mutableStateOf<Resource<String>>(Empty())
    val state: State<Resource<String>> = _state

    init {
        makeSuspendCall(
            block = {
                ""
            },
            onSuccess = {
                _state.value = Resource.Success(it)
            }
        )
    }

}