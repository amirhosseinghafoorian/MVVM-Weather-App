package com.example.mvvmweatherapp.domain

interface LocalRepository {
    suspend fun writeToDataStore(value : String)
    suspend fun readFromDataStore(): String?
}