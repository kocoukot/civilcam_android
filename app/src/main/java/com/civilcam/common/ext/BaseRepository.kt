package com.civilcam.common.ext

import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseRepository {
//    val exceptionErrorMapper = ExceptionErrorMapper()


    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {
                        val jsonObj = JSONObject(throwable.response()?.errorBody()?.string() ?: "")
                        val excep = ServiceException(
                            title = "Something went wrong",
                            errorCode = ServerErrors.byCode(
                                jsonObj["errorCode"].toString().toInt()
                            ),
                            isForceLogout = jsonObj["isForceLogout"].toString().toBoolean(),
                            errorMessage = jsonObj["message"].toString(),
                        )
//                        val httpError = onHttpError(throwable) ?: ServiceException()
                        Resource.Failure(serviceException = excep)
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

    private fun onHttpError(error: HttpException) =
        error.response()?.errorBody()?.let { body ->
            Timber.i("serviceException moshi response ${body.string()}")

            val moshi: Moshi = Moshi.Builder()
//                .add(ErrorCodeAdapter())
                .add(KotlinJsonAdapterFactory())

                .build()

            moshi
                .adapter(ServiceException::class.java)
                .fromJson(body.string())
        }

    class EventJson(
        val errorCode: Int = 0,
        val message: String = "",
        val isForceLogout: Boolean = false
    )

}

class ErrorCodeAdapter {
    @FromJson
    fun fromJson(errorCode: BaseRepository.EventJson): ServiceException {
        Timber.i("serviceException moshi fromJson ${errorCode}")

        return ServiceException(
            errorCode = ServerErrors.byCode(errorCode.errorCode),
            isForceLogout = errorCode.isForceLogout,
            errorMessage = errorCode.message,
        )
    }

    @ToJson
    fun toJson(errorCode: ServiceException) = BaseRepository.EventJson()
}


