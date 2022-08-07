package com.example.mvvmweatherapp.data.repository

import com.example.mvvmweatherapp.BuildConfig
import com.example.mvvmweatherapp.data.remote.WeatherApi
import com.example.mvvmweatherapp.domain.RemoteRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : RemoteRepository {

    override suspend fun getCityLocationFromName(cityName: String): Pair<Double, Double> {
        val result = api.getCityLocation(
            name = cityName,
            apiKey = BuildConfig.API_KEY
        )
        return Pair(
            result[0].lat,
            result[0].lon
        )
    }

    override suspend fun getCityNameFromLocation(latitude: Double, longitude: Double): String {
        val result = api.getCityName(
            latitude = latitude,
            longitude = longitude,
            apiKey = BuildConfig.API_KEY
        )
        return result[0].name
    }

}