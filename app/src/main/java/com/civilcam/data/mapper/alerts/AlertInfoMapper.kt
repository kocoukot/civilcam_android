package com.civilcam.data.mapper.alerts

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.alert.AlertInfoResponse
import com.civilcam.domainLayer.model.alerts.AlertInfoModel

class AlertInfoMapper(
	private val alertModelMapper: AlertListMapper = AlertListMapper(),
	private val alertResolverMapper: AlertResolverMapper = AlertResolverMapper(),
	private val alertDownloadMapper: AlertDownloadMapper = AlertDownloadMapper()
) : Mapper<AlertInfoResponse, AlertInfoModel>(fromData = {
	AlertInfoModel(alertModel = alertModelMapper.mapData(it.alert),
		alertResolvers = it.resolvers.map { resolver -> alertResolverMapper.mapData(resolver) },
		alertDownloads = it.downloads.map { downloadResponse ->
			alertDownloadMapper.mapData(
				downloadResponse
			)
		})
})