package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.ImageInfoMapper
import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.civilcam.domainLayer.model.guard.GuardianStatus
import com.civilcam.domainLayer.model.guard.PersonModel

class PersonMapper(
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
            inputRequest = it.inputRequest?.let { userInputStatus ->
                PersonModel.PersonStatus(
                    statusId = userInputStatus.id,
                    status = GuardianStatus.byDomain(userInputStatus.status)
                )
            },
            outputRequest = it.outputRequest?.let { userOutputStatus ->
                PersonModel.PersonStatus(
                    statusId = userOutputStatus.id,
                    status = GuardianStatus.byDomain(userOutputStatus.status)
                )
            },
            isOnGuard = it.isOnGuard ?: false,
            isGuardian = it.isGuardian ?: false,
        )
    }
)