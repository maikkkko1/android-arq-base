package com.maikkkko1.android_base_arq.arq.extensions


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.maikkkko1.android_base_arq.arq.base.BaseViewModel
import com.maikkkko1.android_base_arq.arq.common.ComponentState
import com.maikkkko1.android_base_arq.arq.common.ViewCommand
import com.maikkkko1.android_base_arq.arq.common.commandObserver
import kotlinx.coroutines.launch

fun AppCompatActivity.openBrowserUrl(url: String) {
    Intent(Intent.ACTION_VIEW, Uri.parse(url)).run {
        startActivity(this)
    }
}

/**
 * Called to observe any live data from your view model.
 */
fun <T> AppCompatActivity.bindData(observable: LiveData<T>, block: (result: T) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindData, Observer {
            block(it)
        })
    }
}

/**
 * Called to observe any component state from your view model.
 */
fun <T> AppCompatActivity.bindState(
        observable: LiveData<ComponentState<T>>,
        block: (result: ComponentState<T>) -> Unit
) = bindData(observable, block)

/**
 * Called to observe all commands sent from your view mode.
 */
fun AppCompatActivity.bindCommand(viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit) {
    lifecycleScope.launch {
        commandObserver(
                lifecycle = this@bindCommand,
                viewModel = viewModel,
                block = block
        )
    }
}