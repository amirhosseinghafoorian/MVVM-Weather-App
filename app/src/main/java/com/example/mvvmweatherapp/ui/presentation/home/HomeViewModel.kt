package com.example.mvvmweatherapp.ui.presentation.home

import androidx.lifecycle.viewModelScope
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.SingleDayForecast
import com.example.mvvmweatherapp.ui.components.InternetNotifier
import com.example.mvvmweatherapp.ui.components.network_state_monitor.NetworkStates
import com.example.mvvmweatherapp.ui.util.BaseViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val internetNotifier: InternetNotifier
) : BaseViewModel() {

    private val _hasSavedLatAndLon = MutableStateFlow<Resource<Unit>>(Resource.Empty())
    val hasSavedLatAndLon = _hasSavedLatAndLon.asStateFlow()

    private val _threeDayForecast =
        MutableStateFlow<Resource<List<SingleDayForecast>>>(Resource.Empty())
    val threeDayForecast = _threeDayForecast.asStateFlow()

    private val _currentDayForecast =
        MutableStateFlow<Resource<List<CurrentForecast>>>(Resource.Empty())
    val currentDayForecast = _currentDayForecast.asStateFlow()

    init {
        getSavedLatAndLon()
        getForecastData()
        observeNetworkChanges()
    }

    private fun observeNetworkChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            internetNotifier.notifyInternetSharedFlow.collect {
                if (it == NetworkStates.Available) {
                    updateForecastData()
                } else if (it == NetworkStates.Lost) {
                    showSnackbar("Internet connection lost")
                }
            }
        }
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