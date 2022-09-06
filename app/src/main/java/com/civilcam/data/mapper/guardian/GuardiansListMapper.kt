package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.UserNetworkResponse
import com.civilcam.domainLayer.model.guard.GuardianItem
import com.civilcam.domainLayer.model.guard.GuardianStatus

class GuardiansListMapper(
    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
) : Mapper<UserNetworkResponse.GuardiansResponse, GuardianItem>(
    fromData = {
        GuardianItem(
            guardianId = it.guardian.id,
            guardianName = it.guardian.fullName,
            guardianAvatar = it.guardian.avatar?.let { avatar -> imageInfoMapper.mapData(avatar) },
            guardianStatus = GuardianStatus.byDomain(it.status),
        )
    }
)


