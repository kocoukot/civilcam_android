package com.civilcam.data.network.support

import com.squareup.moshi.Json

//@JsonClass(generateAdapter = true)
class ServiceException(
    val errorCode: ServerErrors = ServerErrors.SOME_ERROR,
    val title: String? = "Something went wrong",
    val isForceLogout: Boolean? = false,
    @Json(name = "message") val errorMessage: String = ""
) : Throwable(errorMessage)