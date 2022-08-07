package com.example.mvvmweatherapp.data.remote

import com.example.mvvmweatherapp.model.GetCityLocationResponse
import com.example.mvvmweatherapp.model.GetCityNameResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("geo/1.0/direct")
    suspend fun getCityLocation(
        @Query("q") name: String,
        @Query("limit") count: Int = 1,
        @Query("appid") apiKey: String
    ): GetCityLocationResponse

    @GET("geo/1.0/reverse")
    suspend fun getCityName(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("limit") count: Int = 1,
        @Query("appid") apiKey: String
    ): GetCityNameResponse

}