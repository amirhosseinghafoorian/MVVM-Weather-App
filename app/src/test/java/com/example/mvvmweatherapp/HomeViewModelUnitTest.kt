package com.example.mvvmweatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.model.CurrentForecast
import com.example.mvvmweatherapp.model.SingleDayForecast
import com.example.mvvmweatherapp.ui.components.InternetNotifier
import com.example.mvvmweatherapp.ui.presentation.home.HomeViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HomeViewModelUnitTest {

    private val localRepository: LocalRepository = mockk(relaxed = true)
    private val remoteRepository: RemoteRepository = mockk(relaxed = true)
    private val internetNotifier: InternetNotifier = mockk(relaxed = true)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getCurrentForecast_onEmpty() {
        val testCast = listOf<CurrentForecast>()
        coEvery { localRepository.getCurrentForecast() } returns flow {
            testCast
        }

        CoroutineScope(Dispatchers.IO).launch {
            val viewModel = createViewModel()

            coVerify(exactly = 1) { localRepository.getCurrentForecast() }

            assertEquals(
                viewModel.currentDayForecast.value,
                Resource.Empty<CurrentForecast>()
            )
        }
    }

    @Test
    fun getThreeDayForecast_onEmpty() {
        val testCast = listOf<SingleDayForecast>()
        coEvery { localRepository.getThreeDayForecast() } returns flow {
            testCast
        }

        CoroutineScope(Dispatchers.IO).launch {
            val viewModel = createViewModel()

            coVerify(exactly = 1) { localRepository.getThreeDayForecast() }

            assertEquals(
                viewModel.threeDayForecast.value,
                Resource.Empty<SingleDayForecast>()
            )
        }
    }

    @Test
    fun getCurrentForecast_onSuccess() {
        val testCast = listOf(
            CurrentForecast(1, "London", "clear", "23")
        )
        coEvery { localRepository.getCurrentForecast() } returns flow {
            testCast
        }

        CoroutineScope(Dispatchers.IO).launch {
            val viewModel = createViewModel()

            coVerify(exactly = 1) { localRepository.getCurrentForecast() }

            assertEquals(
                viewModel.currentDayForecast.value,
                Resource.Success(testCast)
            )
        }
    }

    @Test
    fun getThreeDayForecast_onSuccess() {
        val testCast = listOf(
            SingleDayForecast(1, "London", "5/17", "23"),
            SingleDayForecast(2, "London", "5/18", "27"),
        )
        coEvery { localRepository.getThreeDayForecast() } returns flow {
            testCast
        }

        CoroutineScope(Dispatchers.IO).launch {
            val viewModel = createViewModel()

            coVerify(exactly = 1) { localRepository.getThreeDayForecast() }

            assertEquals(
                viewModel.threeDayForecast.value,
                Resource.Success(testCast)
            )
        }
    }

    private fun createViewModel(): HomeViewModel {
        return HomeViewModel(
            localRepository,
            remoteRepository,
            internetNotifier
        )
    }
}