package com.civilcam.data.mapper.guardian

import com.civilcam.data.mapper.Mapper
import com.civilcam.data.network.model.response.guardians.UserNetworkResponse
import com.civilcam.domainLayer.model.guard.UserNetworkModel

class UserNetworkMapper(
    private val requestsListMapper: RequestsListMapper = RequestsListMapper(),
    private val onGuardListMapper: OnGuardListMapper = OnGuardListMapper(),
    private val guardiansListMapper: GuardiansListMapper = GuardiansListMapper()
) : Mapper<UserNetworkResponse, UserNetworkModel>(
    fromData = { response ->
        UserNetworkModel(
            requestsList = response.requests.map { requestsListMapper.mapData(it) },
            onGuardList = response.onGuard.map { onGuardListMapper.mapData(it) },
            guardiansList = response.guardians.map { guardiansListMapper.mapData(it) },
        )
    }
)


