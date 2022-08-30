package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.SearchGuardiansResponse
import com.civilcam.domainLayer.model.guard.GuardianModel
import com.civilcam.domainLayer.model.guard.GuardianStatus

class SearchGuardianMapper(
    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
) : Mapper<SearchGuardiansResponse.SearchGuardianResponse, GuardianModel>(
    fromData = {
        GuardianModel(
            guardianId = it.id,
            guardianName = it.fullName,
            guardianAvatar = it.avatar?.let { avatar -> imageInfoMapper.mapData(avatar) },
            guardianStatus = GuardianStatus.NEW,
        )
    }
)