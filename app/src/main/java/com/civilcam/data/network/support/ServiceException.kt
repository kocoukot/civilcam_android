package com.civilcam.data.network.support

class ServiceException(
    val errorCode: ServerErrors = ServerErrors.SOME_ERROR,
    val title: String? = "Something went wrong",
    val isForceLogout: Boolean? = false,
    message: String? = ""
) : Throwable(message)