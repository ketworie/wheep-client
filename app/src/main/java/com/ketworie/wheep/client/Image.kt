package com.ketworie.wheep.client

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


fun loadAvatar(context: Context, imageView: ImageView, address: String) {
    loadAvatar(context, imageView, address) {}
}

fun loadAvatar(context: Context, imageView: ImageView, address: String, onLoaded: (() -> Unit)?) {
    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.raw.icon
    )
    val roundedBitmap = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
    roundedBitmap.isCircular = true
    val circleCrop = Glide.with(context)
        .asBitmap()
        .placeholder(roundedBitmap)
        .circleCrop()
    onLoaded?.let {
        circleCrop.listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                onLoaded.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoaded.invoke()
                return false
            }
        })
    }
    circleCrop
        .load(MainApplication.RESOURCE_BASE + address)
        .into(imageView)
}