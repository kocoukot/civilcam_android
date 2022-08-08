package com.civilcam.domainLayer.model

import com.civilcam.domainLayer.PictureModel
import com.google.android.gms.maps.model.LatLng

data class UserSetupModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var dateBirth: String? = "",
    var profileImage: PictureModel? = null,
    var coords: LatLng? = LatLng(0.0, 0.0), //todo fix remove later
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
//                coords != null

    val isPhotoSelected: Boolean
        get() = profileImage != null

    val isPhoneEntered: Boolean
        get() = phoneNumber?.isNotEmpty() == true
}