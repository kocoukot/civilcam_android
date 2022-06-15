package com.civilcam.common.ext

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val errorCode: Int? = 0,
        val errorTitle: String? = "Something went wrong",
        val errorMessage: String? = "Something went wrong",
        val isForceLogout: Boolean = false
    ) : Resource<Nothing>()

}

