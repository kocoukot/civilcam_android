package com.civilcam.data.ext

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun File.toPart(partName: String): MultipartBody.Part {
    val extension = "image/${
        if (extension == "jpg") {
            "jpeg"
        } else if (extension.isEmpty()) {
            "PNG"
        } else {
            extension
        }
    }"
    return MultipartBody.Part.createFormData(
        partName,
        name,
        asRequestBody((extension.toMediaType()))
    )
}

fun File.toVideoPart(partName: String): MultipartBody.Part {
    val extension = "video/mp4"
    return MultipartBody.Part.createFormData(
        partName,
        name,
        asRequestBody((extension.toMediaType()))
    )
}

