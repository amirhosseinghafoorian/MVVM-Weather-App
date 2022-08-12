package com.example.mvvmweatherapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvmweatherapp.model.CurrentForecastEntity
import com.example.mvvmweatherapp.model.SingleDayForecastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: CurrentForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleDayWeather(vararg singleDayWeather: SingleDayForecastEntity)

    @Query("SELECT * FROM current_weather")
    fun getCurrentWeather(): Flow<List<CurrentForecastEntity>>

    @Query("SELECT * FROM single_day_weather")
    fun getThreeDayForecast(): Flow<List<SingleDayForecastEntity>>

}