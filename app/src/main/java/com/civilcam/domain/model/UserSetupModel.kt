package com.civilcam.domain.model

import com.civilcam.domain.PictureModel
import com.google.android.gms.maps.model.LatLng

data class UserSetupModel(
    var firstName: String? = null,
    var lastName: String? = null,
    var dateBirth: Long? = null,
    var profileImage: PictureModel? = null,
    var coords: LatLng? = LatLng(0.0, 0.0), //todo fix remove later
    var location: String? = null,
    var phoneNumber: String? = null,
) {

    val isFilled: Boolean
        get() =
            firstName?.isNotEmpty() == true &&
                    lastName?.isNotEmpty() == true &&
                    dateBirth != 0L &&
                    profileImage != null &&
                    phoneNumber?.length == 10 &&
                    location?.isNotEmpty() == true
//                coords != null

    val isPhotoSelected: Boolean
        get() = profileImage != null

    val isPhoneEntered: Boolean
        get() = phoneNumber?.isNotEmpty() == true
}

data class SearchModel(
    val searchResult: List<AutocompletePlace> = emptyList(),
    val searchQuery: String = ""
)