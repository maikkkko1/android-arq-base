package com.maikkkko1.android_base_arq.arq.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.imageview.ShapeableImageView
import com.maikkkko1.android_base_arq.arq.extensions.loadFromUrl

@BindingAdapter("loadImageViewFromUrl", requireAll = false)
fun ImageView.loadImageViewFromUrl(url: String?) {
    url?.let { loadFromUrl(url = it) }
}

@BindingAdapter("loadShapeableImageViewFromUrl", requireAll = false)
fun ShapeableImageView.loadShapeableImageViewFromUrl(url: String?) {
    url?.let { loadFromUrl(url = it) }
}