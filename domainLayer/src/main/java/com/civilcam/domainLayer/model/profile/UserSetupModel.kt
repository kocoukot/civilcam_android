package com.civilcam.domainLayer.model.profile

import com.civilcam.domainLayer.PictureModel

data class UserSetupModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var dateBirth: String? = "",
    var profileImage: PictureModel? = null,
    var location: String? = null,
    var phoneNumber: String? = null,
) {

    val isFilled: Boolean
        get() =
            firstName?.isNotEmpty() == true &&
                    lastName?.isNotEmpty() == true &&
                    !dateBirth.isNullOrEmpty() &&
                    profileImage != null &&
                    phoneNumber?.length == 10 &&
                    location?.isNotEmpty() == true

    val isPhotoSelected: Boolean
        get() = profileImage != null

    val isPhoneEntered: Boolean
        get() = phoneNumber?.isNotEmpty() == true
}