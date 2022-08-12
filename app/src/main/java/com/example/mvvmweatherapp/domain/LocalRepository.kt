package com.example.mvvmweatherapp.domain

import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.SingleDayForecast
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getLocationType(): Flow<Boolean?>
    suspend fun changeLocationType(isFromGPS: Boolean)
    suspend fun getSavedLatAndLon(): Flow<Pair<Double?, Double?>>
    suspend fun saveLatAndLon(latitude: Double, longitude: Double)
    suspend fun getCurrentForecast(): Flow<List<CurrentForecast>>
    suspend fun getThreeDayForecast(): Flow<List<SingleDayForecast>>
}