package com.civilcam.domainLayer.model.alerts

import com.civilcam.domainLayer.model.OnGuardUserData
import com.civilcam.domainLayer.model.guard.PersonModel

data class AlertDetailModel(
    val alertModel: AlertModel,
    val alertResolvers: List<AlertResolver>,
    val alertDownloads: List<AlertInfoModel.DownloadInfo>
) {
    class AlertResolver(
        val resolveId: Int,
        val resolveDate: String,
        val userInfo: PersonModel,
    )

    fun toOnGuardDetailModel(): OnGuardUserData =
        OnGuardUserData(
            id = alertModel.alertId,
            date = alertModel.alertDate,
            location = alertModel.alertLocation,
            person = AlertGuardianModel(
                id = alertModel.userInfo?.personId ?: 0,
                language = alertModel.userInfo?.personLanguage.toString(),
                fullName = alertModel.userInfo?.personFullName.orEmpty(),
                avatar = alertModel.userInfo?.personAvatar!!,
                dob = alertModel.userInfo.personBirth.orEmpty(),
                phone = alertModel.userInfo.personPhone.orEmpty(),
                address = alertModel.userInfo.personAddress.orEmpty(),
                latitude = null,
                longitude = null,
            ),
            status = when (alertModel.alertStatus) {
                AlertStatus.ACTIVE -> OnGuardUserData.AlertSocketStatus.active
                AlertStatus.RESOLVED -> OnGuardUserData.AlertSocketStatus.resolved
                AlertStatus.DELETED -> OnGuardUserData.AlertSocketStatus.deleted
            },
        )
}