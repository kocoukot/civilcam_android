package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.mapper.guardian.PersonMapper
import com.civilcam.data.network.model.response.alert.AlertPersonResponse
import com.civilcam.domainLayer.model.alerts.AlertModel
import com.civilcam.domainLayer.model.alerts.AlertStatus

class AlertListMapper(
    private val personMapper: PersonMapper = PersonMapper()
) : Mapper<AlertPersonResponse, AlertModel>(
    fromData = {
        AlertModel(
            alertId = it.id,
            alertDate = it.date,
            alertLocation = it.location,
            alertUrl = it.url,
            alertStatus = AlertStatus.byDomain(it.status),
            userInfo = personMapper.mapData(it.person),
        )
    }
)