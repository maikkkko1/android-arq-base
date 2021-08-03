package com.maikkkko1.android_base_arq.arq.extensions

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.imageview.ShapeableImageView
import com.maikkkko1.android_base_arq.arq.functions.retrieveVideoThumbnail
import com.maikkkko1.android_base_arq.cache.CacheItem
import com.maikkkko1.android_base_arq.cache.CacheManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun ShapeableImageView.loadFromUrl(url: String) {
    loadGlideFromUrl(context = context, url = url, imageView = this)
}

fun ImageView.loadFromUrl(url: String) {
    loadGlideFromUrl(context = context, url = url, imageView = this)
}

fun ImageView.loadVideoUrlThumbnail(url: String, useCache: Boolean, onThumbLoaded: (() -> Unit)? = null) {
    fun setImg(bitmap: Bitmap) {
        if (useCache) CacheManager.add(CacheItem(key = url, data = bitmap))

        setImageBitmap(bitmap)

        onThumbLoaded?.invoke()
    }

    if (useCache) {
        val itemCached = CacheManager.fetch(key = url)

        if (itemCached != null) {
            return setImg((itemCached.data as Bitmap))
        }
    }

    GlobalScope.launch {
        withIO {
            retrieveVideoThumbnail(url)?.let {
                context.getActivity()?.runOnUiThread { setImg(it) }
            }
        }
    }
}

internal fun loadGlideFromUrl(context: Context, url: String, imageView: ImageView) {
    val circularProgressDrawable = getProgressDrawable(context = context, strokeWidth = 5f)
    circularProgressDrawable.start()

    Glide.with(context).load(url).centerCrop().placeholder(circularProgressDrawable).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(imageView)
}

internal fun getProgressDrawable(context: Context, strokeWidth: Float): CircularProgressDrawable {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = strokeWidth
    circularProgressDrawable.centerRadius = 60f

    return circularProgressDrawable
}