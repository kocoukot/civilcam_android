package com.civilcam.common.ext

import com.civilcam.data.network.support.ServiceException

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val serviceException: ServiceException,
    ) : Resource<Nothing>()

}

