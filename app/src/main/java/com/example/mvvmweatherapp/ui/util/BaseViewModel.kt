package com.example.mvvmweatherapp.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {

    private suspend fun <R> runCatching(block: suspend () -> R): Result<R> {
        return try {
            Result.success(block())
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }

    private suspend fun <R> makeSuspendCall(block: suspend () -> R): Result<R> {
        return withContext(Dispatchers.IO) {
            runCatching(block)
        }
    }

    protected fun <R> makeSuspendCall(
        block: suspend () -> R,
        onSuccess: ((R) -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
        onLoading: (() -> Unit)? = null
    ) {
        viewModelScope.launch {

            onLoading?.invoke()
            makeSuspendCall(block).apply {
                try {
                    val result = getOrThrow()
                    onSuccess?.invoke(result)
                } catch (e: Exception) {
                    onError?.invoke(e)
                    e.printStackTrace()
                }
            }

        }
    }

}