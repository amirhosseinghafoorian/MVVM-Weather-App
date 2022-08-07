package com.example.mvvmweatherapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mvvmweatherapp.data.datastore.DataStoreKeys
import com.example.mvvmweatherapp.domain.LocalRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocalRepository {

    override suspend fun writeToDataStore(value: String) {
        dataStore.edit {
            it[DataStoreKeys.locationType] = value
        }
    }

    override suspend fun readFromDataStore(): String? {
        return dataStore.data.first()[DataStoreKeys.locationType]
    }

}