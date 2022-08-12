package com.example.mvvmweatherapp.ui.presentation.home

import android.util.Log
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

    private val _hasSavedLatAndLon = mutableStateOf<Resource<Unit>>(Resource.Empty())
    val hasSavedLatAndLon: State<Resource<Unit>> = _hasSavedLatAndLon

    init {
        getSavedLatAndLon()
        getForecastData()
    }

    private fun getForecastData() {
        makeSuspendCall(
            block = {
                localRepository.getCurrentForecast()
            },
            onSuccess = { flow ->
                viewModelScope.launch {
                    flow.collect { current ->
                        Log.i(
                            "baby",
                            "${current.cityName} , ${current.description}"
                        )
                    }
                }
            }
        )
        makeSuspendCall(
            block = {
                localRepository.getThreeDayForecast()
            },
            onSuccess = { flow ->
                viewModelScope.launch {
                    flow.collect {
                        Log.i(
                            "baby",
                            "size is ${it.size}"
                        )
//                        it.forEach { singleDay ->
//                            Log.i(
//                                "baby",
//                                "day ${singleDay.id} , ${singleDay.date} , ${singleDay.description}"
//                            )
//                        }
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
                            updateDistinct()
                            updateForecastData()
                        } else {
                            _hasSavedLatAndLon.value = Resource.Error("")
                        }
                    }
                }
            }
        )
    }

    private fun updateForecastData() {
        makeSuspendCall(
            block = {
                remoteRepository.updateForecastData()
            }
        )
    }

    private fun updateDistinct() {
        if (hasSavedLatAndLon.value !is Resource.Success) {
            _hasSavedLatAndLon.value = Resource.Success(Unit)
        }
    }

}