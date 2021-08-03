package com.maikkkko1.android_base_arq.arq.extensions

import android.view.View

fun View.animateShow() {
    if (alpha == 0f)
        animate().setDuration(300).alpha(1f).start()
}

fun View.animateHide() {
    if (alpha == 1f)
        animate().setDuration(300).alpha(0f).start()
}

// Combine both of those methods with android:animateLayoutChanges="true" at the root view of your xml
fun View.animateDisappear() {
    visibility = View.GONE
}

fun View.animateAppearance() {
    visibility = View.VISIBLE
}