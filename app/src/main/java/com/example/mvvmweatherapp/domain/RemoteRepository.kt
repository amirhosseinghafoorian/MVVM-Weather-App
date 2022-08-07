package com.example.mvvmweatherapp.domain

interface RemoteRepository {
    suspend fun getCityLocationFromName(cityName: String): Pair<Double, Double>
    suspend fun getCityNameFromLocation(latitude: Double, longitude: Double): String
}