package com.example.mvvmweatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "single_day_weather")
data class SingleDayForecastEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val date: String,
    val temperature: String
)
