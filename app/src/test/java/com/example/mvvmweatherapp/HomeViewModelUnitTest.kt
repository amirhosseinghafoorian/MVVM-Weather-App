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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelUnitTest {

    private val localRepository: LocalRepository = mockk(relaxed = true)
    private val remoteRepository: RemoteRepository = mockk(relaxed = true)
    private val internetNotifier: InternetNotifier = mockk(relaxed = true)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @Test
    fun getCurrentForecast_onSuccess() = runTest {
        val testCast = listOf(
            CurrentForecast(1, "London", "clear", "23")
        )
        coEvery { localRepository.getCurrentForecast() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Success(testCast)::class.java,
                viewModel.currentDayForecast.value::class.java
            )

            assertEquals(
                Resource.Success(testCast).data,
                viewModel.currentDayForecast.value.data
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getCurrentForecast() }
            }
        }
    }

    @Test
    fun getThreeDayForecast_onSuccess() = runTest {
        val testCast = listOf(
            SingleDayForecast(1, "London", "5/17", "23"),
            SingleDayForecast(2, "London", "5/18", "27"),
        )
        coEvery { localRepository.getThreeDayForecast() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Success(testCast)::class.java,
                viewModel.threeDayForecast.value::class.java
            )
            assertEquals(
                Resource.Success(testCast).data,
                viewModel.threeDayForecast.value.data
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getThreeDayForecast() }
            }
        }
    }

    @Test
    fun getSavedLatAndLon_onSuccess() = runTest {
        val testCast = Pair(-67.5, 26.9)
        coEvery { localRepository.getSavedLatAndLon() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Success(Unit)::class.java,
                viewModel.hasSavedLatAndLon.value::class.java
            )
            assertEquals(
                Resource.Success(Unit).data,
                viewModel.hasSavedLatAndLon.value.data
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getSavedLatAndLon() }
            }
        }
    }

    @Test
    fun getCurrentForecast_onEmpty() = runTest {
        val testCast = listOf<CurrentForecast>()
        coEvery { localRepository.getCurrentForecast() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Empty<CurrentForecast>()::class.java,
                viewModel.currentDayForecast.value::class.java
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getCurrentForecast() }
            }
        }
    }

    @Test
    fun getThreeDayForecast_onEmpty() = runTest {
        val testCast = listOf<SingleDayForecast>()
        coEvery { localRepository.getThreeDayForecast() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Empty<SingleDayForecast>()::class.java,
                viewModel.threeDayForecast.value::class.java
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getThreeDayForecast() }
            }
        }
    }

    @Test
    fun getSavedLatAndLon_onError() = runTest {

        coEvery { localRepository.getSavedLatAndLon() } returns flowOf(
            Pair<Double?, Double?>(
                -67.5,
                null
            )
        )

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertSame(
                Resource.Error<Unit>("")::class.java,
                viewModel.hasSavedLatAndLon.value::class.java
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getSavedLatAndLon() }
            }
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