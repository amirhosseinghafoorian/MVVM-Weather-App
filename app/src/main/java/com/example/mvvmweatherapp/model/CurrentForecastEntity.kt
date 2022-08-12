package com.example.mvvmweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentForecastEntity(
    @PrimaryKey val id: Int = 1,
    val cityName: String,
    val description: String,
    val temperature: String
)
