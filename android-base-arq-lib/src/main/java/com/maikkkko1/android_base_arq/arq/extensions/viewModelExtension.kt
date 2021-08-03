package com.maikkkko1.android_base_arq.arq.extensions

import androidx.lifecycle.ViewModel

/**
 * Fetch the SharedViewModel instance, if the instance does not exist yet, returns a new one.
 *
 * @param T the ViewModel type. Ex: UserViewModel, LoginViewModel...
 * @param vm the ViewModel instance itself. Ex: The injectable instance from Koin, Dagger..
 * @return the desired SharedViewModel instance.
 */
inline fun <reified T : Any> fetchSharedViewModel(vm: ViewModel): T {
    with(SharedViewModelHolder) {
        val vmTag = getViewModelTag(vm)
        val viewModelInstance = viewModelHolder[vmTag]

        if (viewModelInstance != null) return viewModelInstance as T

        viewModelHolder[vmTag] = vm

        return vm as T
    }
}

/**
 * Destroy the SharedViewModel instance.
 *
 * @param vm the desired ViewModel to be destroyed.
 */
fun destroySharedViewModel(vm: ViewModel) {
    with(SharedViewModelHolder) {
        val vmTag = getViewModelTag(vm)
        val viewModelInstance = viewModelHolder[vmTag]

        if (viewModelInstance != null) viewModelHolder.remove(vmTag)
    }
}

/**
 * Returns the ViewModel tag.
 *
 * @param vm the desired ViewModel to get the tag.
 */
fun getViewModelTag(vm: ViewModel) = vm::class.java.canonicalName ?: ""

/**
 * Static instance of a object to contain only the SharedViewModel instances.
 */
object SharedViewModelHolder {
    val viewModelHolder: HashMap<String, ViewModel> = HashMap()
}
