package com.example.mvvmweatherapp.ui.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.SingleDayForecast
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

    private val _threeDayForecast =
        mutableStateOf<Resource<List<SingleDayForecast>>>(Resource.Empty())
    val threeDayForecast: State<Resource<List<SingleDayForecast>>> = _threeDayForecast

    private val _currentDayForecast =
        mutableStateOf<Resource<List<CurrentForecast>>>(Resource.Empty())
    val currentDayForecast: State<Resource<List<CurrentForecast>>> = _currentDayForecast

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
                    flow.collect {
                        if (it.isNotEmpty()) _currentDayForecast.value = Resource.Success(it)
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
                        if (it.isNotEmpty()) _threeDayForecast.value = Resource.Success(it)
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