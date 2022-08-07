package com.example.mvvmweatherapp.di

import com.example.mvvmweatherapp.data.repository.RemoteRepositoryImpl
import com.example.mvvmweatherapp.domain.RemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideTestRepository(remoteRepositoryImpl: RemoteRepositoryImpl): RemoteRepository

}