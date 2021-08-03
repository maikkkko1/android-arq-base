package com.maikkkko1.android_base_arq.components.dialog

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomAlertDialog constructor(private val context: Context) {
    fun showSimpleDialog(
        title: String,
        message: String,
        positiveButtonCallback: (() -> Unit)? = null,
        positiveButtonTitle: String? = null
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonTitle ?: "Ok") { dialog: DialogInterface, _ ->
                positiveButtonCallback?.invoke()

                dialog.dismiss()
            }
        }.show()
    }

    fun showConfirmDialog(
        title: String,
        message: String,
        positiveButtonTitle: String,
        positiveButtonCallback: (() -> Unit)? = null,
        negativeButtonTitle: String? = "Cancel",
        negativeButtonCallback: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setMessage(message)
            setNegativeButton(negativeButtonTitle) { dialog: DialogInterface, _ ->
                negativeButtonCallback?.invoke()

                dialog.dismiss()
            }
            setPositiveButton(positiveButtonTitle) { dialog: DialogInterface, _ ->
                positiveButtonCallback?.invoke()

                dialog.dismiss()
            }
        }.show()
    }

    fun showOptionsDialog(
        options: List<String>,
        title: String,
        cancelCallback: (() -> Unit)? = null,
        selectedCallback: ((selected: String) -> Unit)?
    ) {
        MaterialAlertDialogBuilder(context).apply {
            setTitle(title)
            setCancelable(false)
            setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                dialog.dismiss()

                cancelCallback?.invoke()
            }
            setItems(options.toTypedArray()) { dialog, which ->
                dialog.dismiss()

                selectedCallback?.invoke(options[which])
            }
        }.show()
    }
}