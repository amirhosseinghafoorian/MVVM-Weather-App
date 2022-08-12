package com.example.mvvmweatherapp.data.repository

import android.util.Log
import com.example.mvvmweatherapp.BuildConfig
import com.example.mvvmweatherapp.data.remote.WeatherApi
import com.example.mvvmweatherapp.data.util.getDate
import com.example.mvvmweatherapp.data.util.toCelsius
import com.example.mvvmweatherapp.data.util.toLimitedTemp
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val api: WeatherApi,
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

    override suspend fun updateForecastData() {
        val savedLatAndLon = localRepository.getSavedLatAndLon().first()
        if (savedLatAndLon.first != null && savedLatAndLon.second != null) {
            val result = api.getCurrentForecast(
                latitude = savedLatAndLon.first!!,
                longitude = savedLatAndLon.second!!,
                apiKey = BuildConfig.API_KEY
            )
            // todo what we need to save
//            result.name
//            result.weather[0].main
//            result.main.temp.toCelsius()

            val result5Day = api.getFiveDayForecast(
                latitude = savedLatAndLon.first!!,
                longitude = savedLatAndLon.second!!,
                apiKey = BuildConfig.API_KEY
            )

            Log.i(
                "baby", "${result5Day.list[0].dt_txt.getDate()} - ${result5Day.list[0].weather[0].main} : " +
                        "${result5Day.list[0].main.temp_min.toCelsius().toLimitedTemp()} ^C / " +
                        "${result5Day.list[0].main.temp_max.toCelsius().toLimitedTemp()} ^C"
            )
            // todo call 5 day api
            // min and max
            // clear
            // date

        } else {
            // todo maybe throw exception
        }
    }

}