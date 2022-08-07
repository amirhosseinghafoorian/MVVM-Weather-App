package com.example.mvvmweatherapp.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {

    val locationType: Preferences.Key<String> by lazy {
        stringPreferencesKey("locationType")
    }

}