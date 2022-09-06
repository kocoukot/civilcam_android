package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.UserNetworkResponse
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianStatus

class RequestsListMapper(
    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
) : Mapper<UserNetworkResponse.NetworkRequestsResponse, GuardianItem>(
    fromData = {
        GuardianItem(
            guardianId = it.person.id,
            guardianName = it.person.fullName,
            guardianAvatar = it.person.avatar?.let { avatar -> imageInfoMapper.mapData(avatar) },
            guardianStatus = GuardianStatus.byDomain(it.status),
        )
    }
)



