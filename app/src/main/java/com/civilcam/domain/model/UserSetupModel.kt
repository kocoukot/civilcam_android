package com.civilcam.domain.model

import com.civilcam.domain.PictureModel
import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

data class UserSetupModel(
    var firstName: String = "",
    var lastName: String = "",
    var dateBirth: Long = 0,
    var profileImage: PictureModel? = null,
    var coords: LatLng? = LatLng(0.0, 0.0), //todo fix remove later
    var location: String = "",
    var phoneNumber: String = "",

    ) : Serializable {

    val isFilled: Boolean
        get() = firstName.isNotEmpty()
//                &&
//                lastName.isNotEmpty() &&
//                phoneNumber.isNotEmpty() &&
//                location.isNotEmpty() &&
//                profileImage != null &&
//                coords != null

    val isPhotoSelected: Boolean
        get() = profileImage != null

    val isPhoneEntered: Boolean
        get() = phoneNumber.isNotEmpty()
}