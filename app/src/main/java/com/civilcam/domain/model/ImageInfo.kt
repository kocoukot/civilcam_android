package com.civilcam.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageInfo(
    val thumbnailUrl: String?,
    val mimetype: String?,
    val imageUrl: String?,
) : Parcelable