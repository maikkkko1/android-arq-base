package com.maikkkko1.android_base_arq.arq.common


sealed class ComponentState<out T> {
    object Initializing : ComponentState<Nothing>()
    sealed class Loading : ComponentState<Nothing>() {
        object FromEmpty : Loading()
        object FromData : Loading()
    }

    data class Error(val reason: Throwable) : ComponentState<Nothing>()
    data class Success<T>(val result: T) : ComponentState<T>()
}