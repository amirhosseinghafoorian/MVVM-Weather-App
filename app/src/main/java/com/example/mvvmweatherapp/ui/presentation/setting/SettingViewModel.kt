package com.example.mvvmweatherapp.ui.presentation.setting

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.LocationTracker
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import com.example.mvvmweatherapp.ui.util.Resource.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val locationTracker: LocationTracker
) : BaseViewModel() {

    private val _isLocationFromGPS = mutableStateOf<Resource<Boolean>>(Empty())
    val isLocationFromGPS: State<Resource<Boolean>> = _isLocationFromGPS
    // todo maybe not resource

    private val _cityName = mutableStateOf<Resource<String>>(Empty())
    val cityName: State<Resource<String>> = _cityName

    init {
        getLocationType()
        getSavedLatAndLon()
    }

    fun getCurrentLocation() {
        makeSuspendCall(
            block = {
                locationTracker.getCurrentLocation()
            },
            onSuccess = { location ->
                location?.let {
                    saveLatAndLon(it.latitude, it.longitude)
                } ?: run {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        showSnackbar("location currently unavailable for android 12 or higher")
                    } else {
                        showSnackbar("device location is turned off")
                    }
                    changeLocationType(false)
                }
            }
        )
    }

    fun getCityLocation(newCityName: String) {
        if (newCityName.isNotBlank()) {
            makeSuspendCall(
                block = {
                    remoteRepository.getCityLocationFromName(newCityName)
                },
                onSuccess = {
                    saveLatAndLon(it.first, it.second)
                },
                onError = { exception ->
                    viewModelScope.launch {
                        if (cityName.value is Loading) _cityName.value =
                            Error(exception.message.toString())
                    }
                    if (exception is IndexOutOfBoundsException) {
                        showSnackbar("Invalid city name")
                    } else {
                        showSnackbar("Internet problem")
                    }
                },
                onLoading = {
                    viewModelScope.launch {
                        if (cityName.value is Empty) _cityName.value = Loading()
                    }
                }
            )
        } else {
            showSnackbar("city name cannot be empty")
        }
    }

    fun changeLocationType(newLocationType: Boolean) {
        if (newLocationType != isLocationFromGPS.value.data) {
            makeSuspendCall(
                block = {
                    localRepository.changeLocationType(newLocationType)
                }
            )
        }
    }

    private fun getLocationType() {
        makeSuspendCall(
            block = {
                localRepository.getLocationType()
            },
            onSuccess = { resultFlow ->
                viewModelScope.launch {
                    resultFlow.collect { value ->
                        value?.let {
                            _isLocationFromGPS.value = Success(it)
                        } ?: run {
                            _isLocationFromGPS.value = Empty()
                        }
                    }
                }
            }
        )
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
                _cityName.value = Success(it)
            },
            onLoading = {
                _cityName.value = Loading()
            }
        )
    }

    private fun saveLatAndLon(latitude: Double, longitude: Double) {
        makeSuspendCall(
            block = {
                localRepository.saveLatAndLon(latitude, longitude)
            }
        )
    }

}