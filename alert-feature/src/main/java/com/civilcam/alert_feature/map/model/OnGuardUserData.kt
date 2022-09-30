package com.civilcam.alert_feature.map.model

import com.civilcam.domainLayer.model.alerts.AlertGuardianModel

class OnGuardUserData(
    val id: Int,
    val date: String,
    val location: String,
    val person: AlertGuardianModel
)