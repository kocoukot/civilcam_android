package com.civilcam.data.mapper

import com.civilcam.data.network.model.response.ErrorResponse
import com.civilcam.data.network.support.ServerErrors
import com.civilcam.data.network.support.ServiceException

class ErrorMapper : Mapper<ErrorResponse, ServiceException>(
    fromData = {
        ServiceException(
            ServerErrors.byCode(it.errorCode),
            it.title,
            it.isForceLogout,
            it.message
        )
    }
)
