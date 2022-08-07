package com.example.mvvmweatherapp.ui.presentation.setting

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.LocationTracker
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import com.example.mvvmweatherapp.ui.util.Resource.Empty
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    private val locationTracker: LocationTracker
) : BaseViewModel() {

    private val _cityName = mutableStateOf<Resource<String>>(Empty())
    val cityName: State<Resource<String>> = _cityName

    private val _cityLocation = mutableStateOf<Resource<Pair<Double, Double>>>(Empty())
    val cityLocation: State<Resource<Pair<Double, Double>>> = _cityLocation

    fun getCityLocation(cityName: String) {
        makeSuspendCall(
            block = {
                remoteRepository.getCityLocationFromName(cityName)
            },
            onSuccess = {
                _cityLocation.value = Resource.Success(it)
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

    fun getCityName() {
        cityLocation.value.data?.let {
            makeSuspendCall(
                block = {
                    remoteRepository.getCityNameFromLocation(
                        latitude = it.first,
                        longitude = it.second
                    )
                },
                onSuccess = {
                    _cityName.value = Resource.Success(it)
                }
            )
        }
    }

    fun getCurrentLocation() {
        makeSuspendCall(
            block = {
                locationTracker.getCurrentLocation()
            },
            onSuccess = { location ->
                location?.let {
                    // todo location.latitude location.longitude available
                } ?: run {
                    // todo location failed"
                }
            }
        )

    }

    fun writeToDataStore() {
        makeSuspendCall(
            block = {
                localRepository.writeToDataStore("test")
            },
            onSuccess = {
                _cityName.value = Resource.Success("successFully wrote")
            }
        )
    }

    fun readFromDataStore() {
        makeSuspendCall(
            block = {
                localRepository.readFromDataStore()
            },
            onSuccess = { value ->
                value?.let {
                    _cityName.value = Resource.Success("value : $it")
                } ?: run {
                    _cityName.value = Resource.Success("not found")
                }
            }
        )
    }

}