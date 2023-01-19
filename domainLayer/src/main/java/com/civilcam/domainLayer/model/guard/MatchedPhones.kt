package com.civilcam.domainLayer.model.guard

data class MatchedPhones(
    val phone: String,
    val userId: Int,
    val status: GuardianStatus = GuardianStatus.NEW
)
