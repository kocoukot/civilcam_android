package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserBaseInfo

data class UserProfileState(
	val isLoading: Boolean = false,
	var errorText: String = "",
	var data: UserBaseInfo? = null,
	var profileImage: PictureModel? = null,
	val showDatePicker: Boolean = false,
	val birthDate: Long? = null,
	val screenState: UserProfileScreen = UserProfileScreen.PROFILE
) : ComposeFragmentState {

	val isFilled = data?.avatar != null &&
			data?.firstName != "" &&
			data?.lastName != "" &&
			data?.dob?.isNotEmpty() == true &&
			data?.address != ""
}
