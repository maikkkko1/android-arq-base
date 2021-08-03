package com.maikkkko1.android_base_arq.arq.extensions

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.maikkkko1.android_base_arq.R

/** Handle navigation with animations. */
fun NavController.navigateWithAnim(destination: Int, bundle: Bundle? = null) {
    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_left)
        .setExitAnim(R.anim.fadeout)
        .setPopEnterAnim(R.anim.fadein)
        .setPopExitAnim(R.anim.slide_right)
        .build()

    navigate(destination, bundle, navOptions)
}