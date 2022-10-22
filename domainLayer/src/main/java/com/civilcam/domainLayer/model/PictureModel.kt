package com.civilcam.domainLayer.model

import android.net.Uri

data class PictureModel(
    val name: String,
    val uri: Uri,
    val sizeMb: Float
) {

    fun takeIfSize(size: Float) = takeIf { sizeMb <= size }
}
