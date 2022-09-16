package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.mapper.guardian.PersonMapper
import com.civilcam.data.network.model.response.alert.AlertsDetailResponse
import com.civilcam.domainLayer.model.alerts.AlertDetailModel

class AlertResolverMapper(
    private val personMapper: PersonMapper = PersonMapper()
) : Mapper<AlertsDetailResponse.ResolverResponse, AlertDetailModel.AlertResolver>(
    fromData = {
        AlertDetailModel.AlertResolver(
            resolveId = it.id,
            resolveDate = it.date,
            userInfo = personMapper.mapData(it.person),
        )
    }
)