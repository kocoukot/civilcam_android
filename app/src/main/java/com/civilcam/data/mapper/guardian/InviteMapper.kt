package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.InvitesListResponse
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.UserInviteModel

class InviteMapper : Mapper<InvitesListResponse.InviteResponse, UserInviteModel>(
    fromData = {
        UserInviteModel(
            id = it.id,
            phone = it.phone,
            status = GuardianStatus.byDomain(it.status),
        )
    }
)