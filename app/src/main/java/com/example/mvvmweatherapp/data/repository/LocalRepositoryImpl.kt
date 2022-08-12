package com.example.mvvmweatherapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mvvmweatherapp.data.database.WeatherDao
import com.example.mvvmweatherapp.data.datastore.DataStoreKeys
import com.example.mvvmweatherapp.data.util.toCurrentForecast
import com.example.mvvmweatherapp.data.util.toSingleDayForecast
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.SingleDayForecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val dao: WeatherDao,
) : LocalRepository {

    override suspend fun getLocationType(): Flow<Boolean?> {
        return dataStore.data.map {
            it[DataStoreKeys.locationType]
        }
    }

    override suspend fun changeLocationType(isFromGPS: Boolean) {
        dataStore.edit {
            it[DataStoreKeys.locationType] = isFromGPS
        }
    }

    override suspend fun getSavedLatAndLon(): Flow<Pair<Double?, Double?>> {
        return dataStore.data.map {
            it[DataStoreKeys.lat]
        }.combine(
            dataStore.data.map {
                it[DataStoreKeys.lon]
            }
        ) { lat, lon ->
            Pair(lat, lon)
        }
    }

    override suspend fun saveLatAndLon(latitude: Double, longitude: Double) {
        dataStore.edit {
            it[DataStoreKeys.lat] = latitude
            it[DataStoreKeys.lon] = longitude
        }
    }

    override suspend fun getCurrentForecast(): Flow<List<CurrentForecast>> =
        dao.getCurrentWeather().map {
            it.toCurrentForecast()
        }

    override suspend fun getThreeDayForecast(): Flow<List<SingleDayForecast>> =
        dao.getThreeDayForecast().map {
            it.toSingleDayForecast()
        }

}