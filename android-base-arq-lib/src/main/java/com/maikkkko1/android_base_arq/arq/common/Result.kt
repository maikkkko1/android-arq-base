package com.maikkkko1.android_base_arq.arq.common

sealed class Result<T> {
    data class Success<T>(val result: T) : Result<T>()
    data class Failure<T>(val throwable: Throwable) : Result<T>()
}