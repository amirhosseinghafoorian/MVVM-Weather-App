package com.example.mvvmweatherapp.domain

import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun getLocationType(): Flow<Boolean?>
    suspend fun changeLocationType(isFromGPS: Boolean)
    suspend fun getSavedLatAndLon(): Flow<Pair<Double?, Double?>>
    suspend fun saveLatAndLon(latitude: Double, longitude: Double)
}