package com.civilcam.common.ext

import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

private val decimalFormat =
    DecimalFormat("#0.000000", DecimalFormatSymbols.getInstance(Locale.US))

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Any?.castSafe(): T? = this as? T

@Suppress("UNCHECKED_CAST")
inline fun <reified T> Any.cast(): T = this as T

fun Throwable.serviceCast(errorMsg: (String, ServerErrors, Boolean) -> Unit) =
    this.castSafe<ServiceException>()
        ?.let { errorMsg.invoke(it.errorMessage, it.errorCode, it.isForceLogout) }
        ?: kotlin.run {
            errorMsg.invoke(
                this.localizedMessage ?: "Something went wrong",
                ServerErrors.SOME_ERROR,
                false
            )
        }
