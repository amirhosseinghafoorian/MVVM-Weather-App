package com.example.mvvmweatherapp.data.util

fun Double.toCelsius() = (this - 273.15)

fun Double.toLimitedTemp(): String {
    val result = this.toString()
    return if (result.length > 4) {
        result.removeRange(4, result.length - 1)
    } else result
}

fun String.getDate() = this
    .substringAfter('-')
    .substringBefore(' ')
    .replace('-', '/')