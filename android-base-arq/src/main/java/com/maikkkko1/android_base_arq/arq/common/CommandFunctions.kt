package com.maikkkko1.android_base_arq.arq.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.maikkkko1.android_base_arq.arq.base.BaseViewModel


/**
 * It is called when you wanna observe all commands while the lifecycle owner is activated.
 */
fun commandObserver(lifecycle: LifecycleOwner, viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit) {
    ((viewModel.commandObservable) as SingleLiveEvent<ViewCommand>).observe(
            lifecycle::class.java.simpleName,
            lifecycle,
            Observer {
                block(it)
            }
    )
}