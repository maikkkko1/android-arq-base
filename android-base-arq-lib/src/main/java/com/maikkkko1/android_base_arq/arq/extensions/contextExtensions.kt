package com.maikkkko1.android_base_arq.arq.extensions

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun Context.getActivity(): AppCompatActivity {
    if (this is AppCompatActivity) {
        return this
    }

    if (this is android.view.ContextThemeWrapper) {
        return this.baseContext.getActivity()
    }

    if (this is androidx.appcompat.view.ContextThemeWrapper) {
        return this.baseContext.getActivity()
    }

    throw IllegalArgumentException("The provided context is not a Activity")
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Context.isAppInForeground(): Boolean {
    val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val l = mActivityManager.runningAppProcesses
    for (info in l) {
        if (info.uid == applicationInfo.uid && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            return true
        }
    }
    return false
}