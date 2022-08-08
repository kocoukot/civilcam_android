package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.UserBaseInfo


data class UserProfileState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    var data: UserBaseInfo? = null,
    var profileImage: String? = null,
    var address: String = "",
    val showDatePicker: Boolean = false,
    val birthDate: String = "",
    val screenState: UserProfileScreen = UserProfileScreen.PROFILE,
    val searchLocationModel: SearchModel = SearchModel()
) : ComposeFragmentState {

	val isFilled = user?.firstName != null ||
			user?.lastName != null ||
			user?.dateBirth != null ||
			user?.profileImage != null ||
			user?.location != null
}
