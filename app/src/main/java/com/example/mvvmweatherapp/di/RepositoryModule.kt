package com.example.mvvmweatherapp.di

import com.example.mvvmweatherapp.data.repository.LocalRepositoryImpl
import com.example.mvvmweatherapp.data.repository.RemoteRepositoryImpl
import com.example.mvvmweatherapp.domain.LocalRepository
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
    abstract fun provideRemoteRepository(
        remoteRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository

    @Binds
    @Singleton
    abstract fun provideLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository
}