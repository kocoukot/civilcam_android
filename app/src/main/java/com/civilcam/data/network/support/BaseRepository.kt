package com.civilcam.data.network.support

import com.civilcam.common.ext.isMobileOnline
import com.civilcam.domainLayer.ServerErrors
import com.civilcam.domainLayer.ServiceException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        Resource.Failure(
                            serviceException = try {
                                val jsonObj =
                                    JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                                ServiceException(
                                    title = "Something went wrong",
                                    errorCode = ServerErrors.byCode(
                                        jsonObj["errorCode"].toString().toInt()
                                    ),
                                    isForceLogout = jsonObj["isForceLogout"].toString().toBoolean(),
                                    errorMessage = jsonObj["message"].toString(),
                                )
                            } catch (e: Exception) {
                                ServiceException(
                                    title = "Something went wrong",
                                    errorCode = ServerErrors.SOME_ERROR,
                                    isForceLogout = false,
                                )
                            }
                        )
                    }
                    else -> {
                        if (isMobileOnline())
                            Resource.Failure(
                                serviceException = ServiceException(
                                    errorCode = ServerErrors.SOME_ERROR,
                                    title = "Something went wrong",
                                    isForceLogout = false,
                                    errorMessage = throwable.localizedMessage
                                )
                            )
                        else
                            Resource.Failure(
                                serviceException = ServiceException(
                                    errorCode = ServerErrors.NO_INTERNET_CONNECTION,
                                    title = "Your internet connection is lost.",
                                    errorMessage = "Please, restore it and go back to the app",
                                    isForceLogout = false,

                                    )
                            )
                    }
                }
            }
        }
    }
}