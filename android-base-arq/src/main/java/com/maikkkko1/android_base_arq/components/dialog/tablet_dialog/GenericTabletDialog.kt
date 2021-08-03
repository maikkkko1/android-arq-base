package com.maikkkko1.android_base_arq.components.dialog.tablet_dialog

import androidx.fragment.app.Fragment
import com.maikkkko1.android_base_arq.arq.base.BaseDialogFragment

class GenericTabletDialog(targetFragment: Fragment) : DefaultTabletDialog() {
    override var fragment: Fragment? = (targetFragment as? BaseDialogFragment)?.apply {
        dialogDismissAction = { dialog?.dismiss() }
    }
}