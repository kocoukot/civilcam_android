package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.alert.AlertsDetailResponse
import com.civilcam.domainLayer.model.alerts.AlertDetailModel

class AlertDetailMapper(
    private val alertModelMapper: AlertListMapper = AlertListMapper(),
    private val alertResolverMapper: AlertResolverMapper = AlertResolverMapper()
) : Mapper<AlertsDetailResponse, AlertDetailModel>(
    fromData = {
        AlertDetailModel(
            alertModel = alertModelMapper.mapData(it.alert),
            alertResolvers = it.resolvers.map { resolver -> alertResolverMapper.mapData(resolver) },
        )
    }
)