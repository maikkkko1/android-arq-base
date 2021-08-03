package com.maikkkko1.android_base_arq.arq.functions

import android.view.View
import android.widget.TextView

fun hideViews(views: List<View>) {
    views.forEach {
        it.visibility = View.GONE
    }
}

fun showViews(views: List<View>) {
    views.forEach {
        it.visibility = View.VISIBLE
    }
}

fun hideView(view: View?) {
    view?.visibility = View.GONE
}

fun showView(view: View?) {
    view?.visibility = View.VISIBLE
}

fun setOnClickActionToView(views: List<View>, action: (() -> Unit)) {
    views.forEach {
        it.setOnClickListener { action.invoke() }
    }
}

fun setOnClickActionToView(view: View, action: (() -> Unit)) {
    view.setOnClickListener { action.invoke() }
}

fun setTextTo(views: List<TextView>, text: String) {
    views.forEach {
        it.text = text
    }
}