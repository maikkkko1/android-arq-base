package com.maikkkko1.android_base_arq.arq.extensions

import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog

fun AlertDialog.setFullHeight() {
    this.window?.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
}