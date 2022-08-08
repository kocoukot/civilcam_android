package com.civilcam.domainLayer.model.guard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuardianModel(
    val guardianId: Int,
    val guardianName: String,
    val guardianAvatar: Int,
    var guardianStatus: GuardianStatus,
) : Parcelable
