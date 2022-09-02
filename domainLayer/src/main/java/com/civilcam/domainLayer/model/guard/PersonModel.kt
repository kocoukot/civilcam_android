package com.civilcam.domainLayer.model.guard

import android.os.Parcelable
import com.civilcam.domainLayer.model.user.ImageInfo
import com.civilcam.domainLayer.model.user.LanguageType
import kotlinx.parcelize.Parcelize

@Parcelize
data class PersonModel(
    val personId: Int = 0,
    val personLanguage: LanguageType? = null,
    val personFullName: String = "",
    val personBirth: String? = "",
    val personAvatar: ImageInfo? = null,
    val personPhone: String? = "",
    val personAddress: String? = "",
    var personStatus: GuardianStatus = GuardianStatus.NEW,
    val isOnGuard: Boolean? = false,
    val isGuardian: Boolean = false,
) : Parcelable {
    fun mapToItem() = GuardianItem(
        guardianId = personId,
        guardianName = personFullName,
        guardianAvatar = personAvatar,
        guardianStatus = personStatus,
    )
}