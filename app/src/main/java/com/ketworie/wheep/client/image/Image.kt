package com.ketworie.wheep.client.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ketworie.wheep.client.MainApplication
import com.ketworie.wheep.client.R
import com.ketworie.wheep.client.network.GenericError
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.network.toastError
import com.ketworie.wheep.client.startBlinking
import com.ketworie.wheep.client.stopBlinking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun loadAvatar(context: Context, imageView: ImageView, address: String) {
    loadAvatar(context, imageView, address, null)
}

fun loadAvatar(context: Context, imageView: ImageView, address: String, onLoaded: (() -> Unit)?) {
    val bitmap = BitmapFactory.decodeResource(
        context.resources,
        R.raw.icon
    )
    val roundedBitmap = RoundedBitmapDrawableFactory.create(context.resources, bitmap)
    roundedBitmap.isCircular = true
    if (address.isEmpty()) {
        imageView.setImageDrawable(roundedBitmap)
        return
    }
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
                onLoaded()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoaded()
                return false
            }
        })
    }
    circleCrop
        .load(MainApplication.RESOURCE_BASE + address)
        .into(imageView)
}


suspend fun uploadImage(
    context: Context,
    holder: ImageView,
    image: Uri,
    uploader: suspend ((Uri) -> GenericError<String>)
): String = withContext(Dispatchers.IO) {
    withContext(Dispatchers.Main) {
        holder.isEnabled = false
        holder.startBlinking()
    }
    val drawable = image.path?.let { RoundedBitmapDrawableFactory.create(context.resources, it) }
    drawable?.isCircular = true
    val response = uploader(image)
    withContext(Dispatchers.Main) {
        holder.isEnabled = true
        holder.stopBlinking()
    }
    if (response.toastError(context)) return@withContext ""
    withContext(Dispatchers.Main) {
        holder.setImageDrawable(drawable)
    }
    return@withContext (response as NetworkResponse.Success<String>).body
}
