package com.example.mvvmweatherapp.model

data class GetCityLocationResponseItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)