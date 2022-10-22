package com.civilcam.data.network.support

import com.civilcam.domainLayer.ServiceException

sealed class Resource<out T> {
    data class Success<out T>(val value: T) : Resource<T>()
    data class Failure(val serviceException: ServiceException) : Resource<Nothing>() {
        fun checkIfLogOut(needToLogOut: () -> Unit) {
            if (this.serviceException.isForceLogout) {
                needToLogOut.invoke()
            }
        }
    }
}