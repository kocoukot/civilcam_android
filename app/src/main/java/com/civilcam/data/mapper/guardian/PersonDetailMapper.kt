package com.civilcam.data.mapper.guardian

//class PersonDetailMapper(
//    private val imageInfoMapper: ImageInfoMapper = ImageInfoMapper()
//) : Mapper<PersonDetailResponse, PersonModel>(
//    fromData = {
//        PersonModel(
//            personId = it.person.id,
//            personFullName = it.person.fullName,
//            personBirth = it.person.dob,
//            personAvatar = it.person.avatar?.let { avatar -> imageInfoMapper.mapData(avatar) },
//            personPhone = it.person.phone,
//            personAddress = it.person.address,
//            inputRequest = it.inputRequest?.let { userInputStatus ->
//                PersonModel.PersonStatus(
//                    statusId = userInputStatus.id,
//                    status = GuardianStatus.byDomain(userInputStatus.status)
//                )
//            },
//            outputRequest = it.outputRequest?.let { userOutputStatus ->
//                PersonModel.PersonStatus(
//                    statusId = userOutputStatus.id,
//                    status = GuardianStatus.byDomain(userOutputStatus.status)
//                )
//            },
//            isOnGuard = it.person.isOnGuard ?: false,
//            isGuardian = it.person.isGuardian ?: false,
//        )
//    }
//)