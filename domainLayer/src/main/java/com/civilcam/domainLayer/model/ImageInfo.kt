package com.civilcam.domainLayer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageInfo(
    val mimetype: String?,
    val imageUrl: String?,
    val imageKey: String?,
    val thumbnailKey: String?,
    val thumbnailUrl: String?,
) : Parcelable


