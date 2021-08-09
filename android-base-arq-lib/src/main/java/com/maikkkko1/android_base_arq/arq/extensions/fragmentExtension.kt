package com.maikkkko1.android_base_arq.arq.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.maikkkko1.android_base_arq.arq.base.BaseViewModel
import com.maikkkko1.android_base_arq.arq.common.ComponentState
import com.maikkkko1.android_base_arq.arq.common.ViewCommand
import com.maikkkko1.android_base_arq.arq.common.commandObserver
import com.maikkkko1.android_base_arq.arq.components.dialog.CustomAlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun <T> withIO(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO, block)

/**
 * Called to observe any live data from your view model.
 */
fun <T> Fragment.bindData(observable: LiveData<T>, block: (result: T) -> Unit) {
    lifecycleScope.launch {
        observable.observe(this@bindData, Observer {
            block(it)
        })
    }
}

/**
 * Called to observe any component state from your view model.
 */
fun <T> Fragment.bindState(
    observable: LiveData<ComponentState<T>>, block: (result: ComponentState<T>) -> Unit
) = bindData(observable, block)

/**
 * Called to observe all commands sent from your view mode.
 */
fun Fragment.bindCommand(viewModel: BaseViewModel, block: (result: ViewCommand) -> Unit) {
    lifecycleScope.launch {
        commandObserver(
            lifecycle = this@bindCommand, viewModel = viewModel, block = block
        )
    }
}

fun Fragment.navigateBack(finishIfNoBackStack: Boolean = false) {
    if (finishIfNoBackStack) {
        if (findNavController().previousBackStackEntry == null) {
            requireActivity().finish()
        } else {
            findNavController().popBackStack().also { closeKeyboard() }
        }
    } else {
        findNavController().popBackStack().also { closeKeyboard() }
    }
}

fun Fragment.navigateBackTo(destination: Int, finishIfNoBackStack: Boolean = false) {
    if (finishIfNoBackStack) {
        if (findNavController().previousBackStackEntry == null) {
            requireActivity().finish()
        } else {
            findNavController().popBackStack(destination, false).also { closeKeyboard() }
        }
    } else {
        findNavController().popBackStack(destination, false).also { closeKeyboard() }
    }
}

fun Fragment.navigateTo(destination: Int, bundle: Bundle? = null) {
    val destinationId = findNavController().currentDestination?.getAction(destination)?.destinationId ?: 0

    findNavController().currentDestination?.let { node ->
        val currentNode = when (node) {
            is NavGraph -> node
            else -> node.parent
        }

        if (destinationId != 0) {
            currentNode?.findNode(destinationId)?.let { findNavController().navigateWithAnim(destination, bundle) }
        }
    }
}

fun Fragment.closeKeyboard() {
    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.getResourcesDrawable(id: Int): Drawable? = ContextCompat.getDrawable(requireContext(), id)

fun Fragment.showToast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()

fun Fragment.showSnack(text: String) = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()

inline fun <reified T : Activity> Fragment.getContainerActivity(): T = (requireActivity() as T)

fun Fragment.getRecyclerLayoutManager(tabletSpanCount: Int, isTablet: Boolean): GridLayoutManager? {
    return if (isTablet) GridLayoutManager(
        requireContext(), tabletSpanCount
    ) else null
}

fun Fragment.getResourcesColor(id: Int): Int = ContextCompat.getColor(requireContext(), id)

fun Fragment.showIncorrectInfoDialog(text: String? = null) {
    getCustomAlertDialog().showSimpleDialog(
        "Please check again", text ?: "Incorrect information!"
    )
}

fun Fragment.showErrorDialog(text: String? = null) {
    getCustomAlertDialog().showSimpleDialog(
        title = "Oops", message = text ?: "Unexpected error!"
    )
}

fun Fragment.isAppNotificationsEnabled(): Boolean = NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()

fun Fragment.getCustomAlertDialog() = CustomAlertDialog(requireContext())

fun Fragment.openGallery(actionCode: Int, resourceType: String = "image") {
    Intent().apply {
        type = "$resourceType/*"
        action = Intent.ACTION_GET_CONTENT
    }.run {
        startActivityForResult(Intent.createChooser(this, "Select ${resourceType.capitalize()}"), actionCode)
    }
}

fun Fragment.openTakePhoto(actionCode: Int) {
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).run {
        startActivityForResult(this, actionCode)
    }
}

fun Fragment.getViewId(id: String): Int {
    return resources.getIdentifier(id, "id", context?.packageName)
}