package com.example.mvvmweatherapp.domain

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}