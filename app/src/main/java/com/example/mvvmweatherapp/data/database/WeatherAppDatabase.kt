package com.example.mvvmweatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmweatherapp.model.CurrentForecastEntity
import com.example.mvvmweatherapp.model.SingleDayForecastEntity

@Database(
    entities = [CurrentForecastEntity::class, SingleDayForecastEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherAppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}