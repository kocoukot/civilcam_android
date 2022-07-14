package com.civilcam.domain.model

import com.civilcam.domain.PictureModel
import com.google.android.gms.maps.model.LatLng

data class UserSetupModel(
    var firstName: String = "",
    var lastName: String = "",
    var dateBirth: Long = 0,
    var profileImage: PictureModel? = null,
    var coords: LatLng? = LatLng(0.0, 0.0), //todo fix remove later
    var location: String = "",
    var phoneNumber: String = "",
) {

    val isFilled: Boolean
        get() = firstName.isNotEmpty() &&
                lastName.isNotEmpty() &&
                dateBirth != 0L &&
                profileImage != null &&
                phoneNumber.length == 10 &&
                location.isNotEmpty()
//                coords != null

    val isPhotoSelected: Boolean
        get() = profileImage != null

    val isPhoneEntered: Boolean
        get() = phoneNumber.isNotEmpty()
}

data class SearchModel(
    val searchResult: List<AutocompletePlace> = emptyList(),
    val searchQuery: String = ""
)