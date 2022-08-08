package com.example.mvvmweatherapp.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

    val locationType: Preferences.Key<Boolean> by lazy {
        booleanPreferencesKey("locationType")
    }

    val lat: Preferences.Key<Double> by lazy {
        doublePreferencesKey("lat")
    }

    val lon: Preferences.Key<Double> by lazy {
        doublePreferencesKey("lon")
    }

}