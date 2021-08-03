package com.maikkkko1.android_base_arq.arq.extensions

import android.widget.Button

fun Button.disableButton(newTitle: String? = null) {
    newTitle?.let { text = newTitle }
    isEnabled = false
    alpha = 0.8f
}

fun Button.enableButton(newTitle: String? = null) {
    newTitle?.let { text = newTitle }
    isEnabled = true
    alpha = 1f
}
