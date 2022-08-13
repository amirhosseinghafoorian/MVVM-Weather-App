package com.example.mvvmweatherapp.ui.components

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mvvmweatherapp.domain.RemoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UpdateWeatherWorker @AssistedInject constructor(
    private val remoteRepository: RemoteRepository,
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                remoteRepository.updateForecastData()
                Result.success()
            } catch (e: Exception) {
                Result.Failure()
            }
        }
    }

}