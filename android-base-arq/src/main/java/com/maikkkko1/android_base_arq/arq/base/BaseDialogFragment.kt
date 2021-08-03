package com.maikkkko1.android_base_arq.arq.base

import com.maikkkko1.android_base_arq.arq.extensions.navigateBack

open class BaseDialogFragment(private val isTablet: Boolean = false) : BaseFragment() {
    var dialogDismissAction: (() -> Unit)? = null

    protected fun handleBackButton() {
        if (!isTablet) navigateBack() else dialogDismissAction?.invoke()
    }
}