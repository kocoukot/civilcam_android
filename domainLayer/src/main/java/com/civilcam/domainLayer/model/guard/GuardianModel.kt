package com.civilcam.domainLayer.model.guard

import android.os.Parcelable
import com.civilcam.domainLayer.model.user.ImageInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class GuardianModel(
    val guardianId: Int,
    val guardianName: String,
    val guardianAvatar: ImageInfo?,
    var guardianStatus: GuardianStatus,
    val isOnGuard: Boolean?,
    val isGuardian: Boolean?,
) : Parcelable {
    fun mapToItem() = GuardianItem(
        guardianId = guardianId,
        guardianName = guardianName,
        guardianAvatar = guardianAvatar,
        guardianStatus = guardianStatus,
    )
}