package com.example.mvvmweatherapp.data.repository

import com.example.mvvmweatherapp.BuildConfig
import com.example.mvvmweatherapp.data.database.WeatherDao
import com.example.mvvmweatherapp.data.remote.WeatherApi
import com.example.mvvmweatherapp.data.util.getDate
import com.example.mvvmweatherapp.data.util.toCelsius
import com.example.mvvmweatherapp.data.util.toLimitedTemp
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.model.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(
    private val localRepository: LocalRepository,
    private val api: WeatherApi,
    private val dao: WeatherDao,
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

            val resultCurrent = api.getCurrentForecast(
                latitude = savedLatAndLon.first!!,
                longitude = savedLatAndLon.second!!,
                apiKey = BuildConfig.API_KEY
            )
            insertCurrentForecast(resultCurrent)

            val resultFiveDay = api.getFiveDayForecast(
                latitude = savedLatAndLon.first!!,
                longitude = savedLatAndLon.second!!,
                apiKey = BuildConfig.API_KEY
            )
            insertThreeDayForecast(resultFiveDay)

        }
    }

    private suspend fun insertCurrentForecast(forecast: GetCurrentForecastResponse) {
        dao.insertCurrentWeather(
            CurrentForecastEntity(
                cityName = forecast.name,
                description = forecast.weather[0].main,
                temperature = forecast.main.temp.toCelsius().toLimitedTemp()
            )
        )
    }

    private suspend fun insertThreeDayForecast(forecast: GetFiveDayForecastResponse) {
        val threeDayIndex = getEachDayFirstIndex(forecast)

        dao.insertSingleDayWeather(
            SingleDayForecastEntity(
                id = 1,
                description = forecast.list[threeDayIndex.first].weather[0].main,
                date = forecast.list[threeDayIndex.first].dt_txt.getDate(),
                temperature = forecast.list[threeDayIndex.first].main.temp.toCelsius()
                    .toLimitedTemp(),
            ),
            SingleDayForecastEntity(
                id = 2,
                description = forecast.list[threeDayIndex.second].weather[0].main,
                date = forecast.list[threeDayIndex.second].dt_txt.getDate(),
                temperature = forecast.list[threeDayIndex.second].main.temp.toCelsius()
                    .toLimitedTemp(),
            ),
            SingleDayForecastEntity(
                id = 3,
                description = forecast.list[threeDayIndex.third].weather[0].main,
                date = forecast.list[threeDayIndex.third].dt_txt.getDate(),
                temperature = forecast.list[threeDayIndex.third].main.temp.toCelsius()
                    .toLimitedTemp(),
            )
        )
    }

    private fun getEachDayFirstIndex(forecast: GetFiveDayForecastResponse): Triple<Int, Int, Int> {

        val currentDay = forecast.list[0].dt_txt.getDate()

        val firstDay = findFirstDistinctIndex(forecast.list, 0, currentDay)
        val secondDay = findFirstDistinctIndex(forecast.list, firstDay.first, firstDay.second)
        val thirdDay = findFirstDistinctIndex(forecast.list, secondDay.first, secondDay.second)

        return Triple(firstDay.first, secondDay.first, thirdDay.first)
    }

    private fun findFirstDistinctIndex(
        list: List<SingleHourWeather>,
        startIndex: Int,
        checkingValue: String
    ): Pair<Int, String> {
        for (i in startIndex until list.size) {
            if (list[i].dt_txt.getDate() != checkingValue) {
                return Pair(i, list[i].dt_txt.getDate())
            }
        }
        throw Exception("not found")
    }

}