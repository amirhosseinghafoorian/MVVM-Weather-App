package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : BaseViewModel() {

    private val _cityName = mutableStateOf<Resource<String>>(Resource.Empty())
    val cityName: State<Resource<String>> = _cityName

    init {
        getSavedLatAndLon()
    }

    private fun getSavedLatAndLon() {
        makeSuspendCall(
            block = {
                localRepository.getSavedLatAndLon()
            },
            onSuccess = { latAndLonFlow ->
                viewModelScope.launch {
                    latAndLonFlow.collect { latAndLon ->
                        if (latAndLon.first != null && latAndLon.second != null) {
                            getCityName(latAndLon.first!!, latAndLon.second!!)
                        }
                    }
                }
            }
        )
    }

    private fun getCityName(latitude: Double, longitude: Double) {
        makeSuspendCall(
            block = {
                remoteRepository.getCityNameFromLocation(latitude, longitude)
            },
            onSuccess = {
                _cityName.value = Resource.Success(it)
            },
            onLoading = {
                _cityName.value = Resource.Loading()
            }
        )
    }

}