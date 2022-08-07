package com.example.mvvmweatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.mvvmweatherapp.data.database.WeatherAppDatabase
import com.example.mvvmweatherapp.data.database.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherAppDb(
        @ApplicationContext context: Context
    ): WeatherAppDatabase = Room
        .databaseBuilder(
            context,
            WeatherAppDatabase::class.java,
            "WeatherAppDB.db"
        )
        .build()

    @Provides
    @Singleton
    fun provideWeatherDao(weatherAppDatabase: WeatherAppDatabase): WeatherDao {
        return weatherAppDatabase.weatherDao()
    }
}