package com.maikkkko1.android_base_arq.arq.extensions

fun List<Any?>.isAllNull(): Boolean {
    for (el: Any? in this) {
        if (el != null) return false
    }

    return true
}

fun List<String?>.isAllNullOrEmpty(): Boolean {
    for (el in this) {
        if (!el.isNullOrEmpty()) return false
    }

    return true
}
