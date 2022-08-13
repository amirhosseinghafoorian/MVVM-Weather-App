package com.example.mvvmweatherapp.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.work.WorkManager
import com.example.mvvmweatherapp.ui.components.InternetNotifier
import com.example.mvvmweatherapp.ui.components.network_broadcast_receiver.NetworkChangeReceiver
import com.example.mvvmweatherapp.ui.components.network_state_monitor.NetworkMonitorCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("APP_Preferences")
        }
    }

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        val result by lazy {
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        }
        return result
    }

    @Provides
    @Singleton
    fun provideNetworkMonitorCallback(
        connectivityManager: ConnectivityManager,
        internetNotifier: InternetNotifier
    ): NetworkMonitorCallback {
        val result by lazy {
            NetworkMonitorCallback(connectivityManager, internetNotifier)
        }
        return result
    }

    @Provides
    @Singleton
    fun profileNetworkChangeReceiver(
        internetNotifier: InternetNotifier
    ): NetworkChangeReceiver {
        val result by lazy {
            NetworkChangeReceiver(internetNotifier)
        }
        return result
    }

    @Provides
    @Singleton
    fun provideInternetNotifier() = InternetNotifier()

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ) = WorkManager.getInstance(context)

}