package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel

class SearchGuardianMapper(
    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
) : Mapper<PersonResponse, PersonModel>(
    fromData = {
        PersonModel(
            personId = it.id,
            personFullName = it.fullName,
            personBirth = it.dob,
            personAvatar = it.avatar?.let { avatar -> imageInfoMapper.mapData(avatar) },
            personPhone = it.phone,
            personAddress = it.address,
            personStatus = it.request?.status?.let { domain -> GuardianStatus.byDomain(domain) }
                ?: GuardianStatus.NEW,
            isOnGuard = it.isOnGuard ?: false,
            isGuardian = it.isGuardian ?: false,
        )
    }
)