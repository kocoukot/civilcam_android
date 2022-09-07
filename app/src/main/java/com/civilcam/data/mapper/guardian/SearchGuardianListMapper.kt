package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.civilcam.domainLayer.model.guard.PersonModel

class SearchGuardianListMapper(
    private val personMapper: PersonMapper = PersonMapper()
) : Mapper<List<PersonResponse>, List<PersonModel>>(
    fromData = { response ->
        response.map { personMapper.mapData(it) }
    }
)