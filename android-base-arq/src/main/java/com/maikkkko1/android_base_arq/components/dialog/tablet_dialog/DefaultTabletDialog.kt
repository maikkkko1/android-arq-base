package com.maikkkko1.android_base_arq.components.dialog.tablet_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.maikkkko1.android_base_arq.R
import com.maikkkko1.android_base_arq.arq.extensions.dpToPx
import com.maikkkko1.android_base_arq.databinding.DialogDefaultTabletBinding

open class DefaultTabletDialog(dialogWidth: Int?) : DialogFragment() {
    private var binding: DialogDefaultTabletBinding? = null

    protected open var fragment: Fragment? = null
    protected open var dialogWidth: Int = dialogWidth ?: 400

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return DialogDefaultTabletBinding.inflate(inflater, container, false).let {
            binding = it
            binding?.lifecycleOwner = this
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (fragment != null) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.fragment_container, fragment!!)
            }.commit()
        }

        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_shape))
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes.apply {
            width = requireContext().dpToPx(dialogWidth)
            height = ViewGroup.LayoutParams.MATCH_PARENT
        }

        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
}