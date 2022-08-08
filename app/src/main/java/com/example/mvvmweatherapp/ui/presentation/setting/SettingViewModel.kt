package com.example.mvvmweatherapp.ui.presentation.setting

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

    private val _cityName = mutableStateOf<Resource<String>>(Empty())
    val cityName: State<Resource<String>> = _cityName

    private val _cityLocation = mutableStateOf<Resource<Pair<Double, Double>>>(Empty())
    val cityLocation: State<Resource<Pair<Double, Double>>> = _cityLocation

    init {
        getLocationType()
        getSavedLatAndLon()
    }

    fun getCityName(latitude: Double, longitude: Double) {
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

    fun getCityLocation(cityName: String) {
        makeSuspendCall(
            block = {
                remoteRepository.getCityLocationFromName(cityName)
            },
            onSuccess = {
                _cityLocation.value = Success(it) // todo should be changed
                saveLatAndLon(it.first, it.second)
            },
            onError = { exception ->
                if (exception is IndexOutOfBoundsException) {
                    // todo Invalid cityName
                } else {
                    // todo Internet problem
                }
            }
        )
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
                    // todo location failed"
                }
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

    fun changeLocationType(newLocationType: Boolean) {
        if (newLocationType != isLocationFromGPS.value.data) {
            makeSuspendCall(
                block = {
                    localRepository.changeLocationType(newLocationType)
                }
            )
        }
    }

}