package com.example.mvvmweatherapp.data.util

import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.CurrentForecastEntity
import com.example.mvvmweatherapp.model.SingleDayForecast
import com.example.mvvmweatherapp.model.SingleDayForecastEntity

fun Double.toCelsius() = (this - 273.15)

fun Double.toLimitedTemp(): String {
    val result = this.toString()
    return if (result.length > 4) {
        result.removeRange(4, result.length - 1)
    } else result
}

fun String.getDate() = this
    .substringAfter('-')
    .substringBefore(' ')
    .replace('-', '/')

fun CurrentForecastEntity.toCurrentForecast() =
    CurrentForecast(
        cityName = cityName,
        description = description,
        temperature = temperature
    )

fun List<CurrentForecastEntity>.toCurrentForecast(): List<CurrentForecast> {
    return this.map {
        it.toCurrentForecast()
    }
}

fun SingleDayForecastEntity.toSingleDayForecast() =
    SingleDayForecast(
        id = id,
        description = description,
        date = date,
        temperature = temperature
    )

fun List<SingleDayForecastEntity>.toSingleDayForecast(): List<SingleDayForecast> {
    return this.map {
        it.toSingleDayForecast()
    }
}