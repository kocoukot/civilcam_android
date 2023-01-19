package com.civilcam.data.local.model

import com.civilcam.domainLayer.model.guard.GuardianStatus

data class Contact(
    val name: String,
    val phoneNumber: String,
    val avatar: String,
    val isMyContact: Boolean = false,
    var contactId: Int = 0,
    var status: GuardianStatus = GuardianStatus.NEW
)
