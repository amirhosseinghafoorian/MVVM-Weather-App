package com.example.mvvmweatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmweatherapp.domain.LocalRepository
import com.example.mvvmweatherapp.domain.LocationTracker
import com.example.mvvmweatherapp.domain.RemoteRepository
import com.example.mvvmweatherapp.ui.presentation.setting.SettingViewModel
import com.example.mvvmweatherapp.ui.util.Resource
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SettingViewModelUnitTest {

    private val localRepository: LocalRepository = mockk(relaxed = true)
    private val remoteRepository: RemoteRepository = mockk(relaxed = true)
    private val locationTracker: LocationTracker = mockk(relaxed = true)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getCurrentLocation_isCalled() = runTest {
        coJustRun { locationTracker.getCurrentLocation() }

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            viewModel.getCurrentLocation()

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { locationTracker.getCurrentLocation() }
            }
        }
    }

    @Test
    fun getCityLocationFromName_onEmptyCityName() = runTest {
        coJustRun { remoteRepository.getCityLocationFromName(any()) }

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            viewModel.getCityLocation("")

            withContext(Dispatchers.IO) {
                coVerify(exactly = 0) { remoteRepository.getCityLocationFromName(any()) }
            }
        }
    }

    @Test
    fun getCityLocationFromName_onSuccess() = runTest {
        val testCast = Pair(-67.5, 26.9)
        coEvery { remoteRepository.getCityLocationFromName(any()) } returns testCast

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            viewModel.getCityLocation("London")

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { remoteRepository.getCityLocationFromName("London") }
                coVerify(exactly = 1) {
                    localRepository.saveLatAndLon(
                        testCast.first,
                        testCast.second
                    )
                }
            }
        }
    }

    @Test
    fun getCityLocationFromName_onError() = runTest {
        coEvery { remoteRepository.getCityLocationFromName(any()) } throws Exception("Invalid")

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            viewModel.getCityLocation("London")

            delay(100)

            assertEquals(
                Resource.Error::class.java,
                viewModel.cityName.value::class.java
            )
            assertEquals(
                Resource.Error<String>("Invalid").message,
                viewModel.cityName.value.message
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { remoteRepository.getCityLocationFromName("London") }
                coVerify(exactly = 0) { localRepository.saveLatAndLon(any(), any()) }
            }
        }
    }

    @Test
    fun getLocationType_onSuccess() = runTest {
        val testCase = true
        coEvery { localRepository.getLocationType() } returns flowOf(testCase)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Success::class.java,
                viewModel.isLocationFromGPS.value::class.java
            )
            assertEquals(
                Resource.Success(testCase).data,
                viewModel.isLocationFromGPS.value.data
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getLocationType() }
            }
        }
    }

    @Test
    fun getLocationType_onEmpty() = runTest {
        coEvery { localRepository.getLocationType() } returns flowOf<Boolean?>(null)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Empty::class.java,
                viewModel.isLocationFromGPS.value::class.java
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getLocationType() }
            }
        }
    }

    @Test
    fun getSavedLatAndLon_onSuccess() = runTest {
        val testCast = Pair(-67.5, 26.9)
        coEvery { localRepository.getSavedLatAndLon() } returns flowOf(testCast)
        coEvery {
            remoteRepository.getCityNameFromLocation(
                testCast.first,
                testCast.second
            )
        } returns "London"

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Success::class.java,
                viewModel.cityName.value::class.java
            )
            assertEquals(
                Resource.Success("London").data,
                viewModel.cityName.value.data
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getSavedLatAndLon() }
                coVerify(exactly = 1) {
                    remoteRepository.getCityNameFromLocation(
                        testCast.first,
                        testCast.second
                    )
                }
            }
        }
    }

    @Test
    fun getSavedLatAndLon_onEmpty() = runTest {
        val testCast = Pair<Double?,Double?>(null, 26.9)
        coEvery { localRepository.getSavedLatAndLon() } returns flowOf(testCast)

        Dispatchers.setMain(mainThreadSurrogate)
        launch(Dispatchers.Main) {

            val viewModel = createViewModel()

            delay(100)

            assertEquals(
                Resource.Empty::class.java,
                viewModel.cityName.value::class.java
            )

            withContext(Dispatchers.IO) {
                coVerify(exactly = 1) { localRepository.getSavedLatAndLon() }
                coVerify(exactly = 0) {
                    remoteRepository.getCityNameFromLocation(any(), any())
                }
            }
        }
    }

    private fun createViewModel(): SettingViewModel {
        return SettingViewModel(
            remoteRepository,
            localRepository,
            locationTracker
        )
    }
}