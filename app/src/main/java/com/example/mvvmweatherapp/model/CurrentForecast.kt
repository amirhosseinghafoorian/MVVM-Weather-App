package com.example.mvvmweatherapp.model

data class CurrentForecast(
    val id: Int = 1,
    val cityName: String,
    val description: String,
    val temperature: String
)
