package com.civilcam.domain.model.guard

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuardianModel(
    val guardianId: Int,
    val guardianName: String,
    val guardianAvatar: Int,
    val guardianStatus: GuardianStatus,
) : Parcelable
