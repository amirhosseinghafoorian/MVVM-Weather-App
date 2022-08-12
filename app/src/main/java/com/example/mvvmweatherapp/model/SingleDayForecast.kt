package com.example.mvvmweatherapp.model

data class SingleDayForecast(
    val id: Int,
    val description: String,
    val date: String,
    val temperature: String
)
