package com.maikkkko1.android_base_arq.arq.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.maikkkko1.android_base_arq.arq.extensions.destroySharedViewModel

open class BaseFragment : Fragment() {
    /**
     * Destroy this VM in the onDestroy event.
     */
    open var sharedViewModelToDestroy: ViewModel? = null

    override fun onDestroy() {
        super.onDestroy()

        sharedViewModelToDestroy?.let { destroySharedViewModel(it) }
    }
}