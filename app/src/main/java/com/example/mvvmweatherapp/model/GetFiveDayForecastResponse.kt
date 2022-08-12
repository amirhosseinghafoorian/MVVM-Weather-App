package com.example.mvvmweatherapp.model

data class GetFiveDayForecastResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<SingleHourWeather>,
    val message: Int
)