package com.maikkkko1.android_base_arq.arq.extensions

fun String.pluralize(count: Int, plural: String? = null): String {
    return if (count > 1) {
        plural ?: this + 's'
    } else {
        this
    }
}