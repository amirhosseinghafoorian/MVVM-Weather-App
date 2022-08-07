package com.example.mvvmweatherapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmweatherapp.model.TestEntity

@Database(
    entities = [TestEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WeatherAppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}