package com.maikkkko1.android_base_arq.arq.base

import com.maikkkko1.android_base_arq.arq.extensions.navigateBack

open class BaseDialogFragment : BaseFragment() {
    var dialogDismissAction: (() -> Unit)? = null

    var isTabletDialog = false

    protected fun handleBackButton() {
        if (!isTabletDialog) navigateBack() else dialogDismissAction?.invoke()
    }
}