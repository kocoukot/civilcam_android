package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.PersonResponse
import com.civilcam.domainLayer.model.guard.GuardianModel

class SearchGuardianListMapper(
    private val searchGuardianMapper: SearchGuardianMapper = SearchGuardianMapper()
) : Mapper<List<PersonResponse>, List<GuardianModel>>(
    fromData = { response ->
        response.map {
            searchGuardianMapper.mapData(it)
        }
    }
)