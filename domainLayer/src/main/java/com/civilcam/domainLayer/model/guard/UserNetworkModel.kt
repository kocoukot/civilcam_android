package com.civilcam.domainLayer.model.guard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserNetworkModel(
    val requestsList: List<GuardianItem>,
    val onGuardList: List<GuardianItem>,
    val guardiansList: List<GuardianItem>,
) : Parcelable
