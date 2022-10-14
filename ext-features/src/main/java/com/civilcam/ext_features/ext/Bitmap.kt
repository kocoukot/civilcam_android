package com.civilcam.ext_features.ext

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

fun loadIcon(
    context: Context,
    url: String?,
    placeHolder: Int,
): BitmapDescriptor? {
    try {
        var bitmap: Bitmap? = null
        Glide.with(context)
            .asBitmap()
            .load(url)
            .error(placeHolder)
            // to show a default icon in case of any errors
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {

                    bitmap = getRoundedCornerBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        return BitmapDescriptorFactory.fromBitmap(bitmap!!)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

private fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap? {
    val bitMapSize = 40.toDp()
    val odds = 2.toDp()

    val backgroundCircle = Bitmap.createBitmap(
        bitMapSize,
        bitMapSize, Bitmap.Config.ARGB_8888
    )
    backgroundCircle.eraseColor(Color.RED)

    val outputBitmap = Bitmap.createBitmap(
        backgroundCircle.width,
        backgroundCircle.height,
        Bitmap.Config.ARGB_8888
    )
    val resizedAvatar =
        getRoundedBitmap(Bitmap.createScaledBitmap(bitmap, bitMapSize, bitMapSize, true))
    val resizedBackground =
        getRoundedBitmap(Bitmap.createScaledBitmap(backgroundCircle, bitMapSize, bitMapSize, true))

    val outputCanvas = Canvas(outputBitmap)

    val paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
    }


    val outerRect = Rect(0, 0, resizedBackground.height, resizedBackground.width)
    val innerRect =
        Rect(odds, odds, resizedBackground.height - odds, resizedBackground.width - odds)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.ADD)


    outputCanvas.drawBitmap(resizedBackground, outerRect, outerRect, paint)
    outputCanvas.drawBitmap(resizedAvatar, outerRect, innerRect, null)
    return outputBitmap
}

private fun getRoundedBitmap(bitmap: Bitmap): Bitmap {
    val resultBitmap: Bitmap
    val originalWidth = bitmap.width
    val originalHeight = bitmap.height
    val r: Float = (originalHeight).toFloat()
    resultBitmap = Bitmap.createBitmap(
        originalHeight, originalHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(resultBitmap)
    val paint = Paint().apply {
    }
    val rect = Rect(
        0,
        0, originalWidth, originalHeight
    )
    paint.isAntiAlias = true

    val rect1 = Rect(0, 0, resultBitmap.height, resultBitmap.width)

    val rectF = RectF(rect1)


    canvas.drawRoundRect(rectF, r, r, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(bitmap, rect, rect, paint)
    return resultBitmap
}