package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.UserSetupModel
import com.civilcam.ui.profile.userDetails.model.UserDetailsModel

data class UserProfileState(
	val isLoading: Boolean = false,
	val errorText: String = "",
	var data: UserDetailsModel? = null,
	var profileImage: PictureModel? = null,
	val showDatePicker: Boolean = false,
	val birthDate: Long? = null,
	val screenState: UserProfileScreen = UserProfileScreen.PROFILE
) : ComposeFragmentState {
	
	val isFilled = data?.userInfoSection?.avatar != 0 &&
			data?.userInfoSection?.firstName != "" &&
			data?.userInfoSection?.lastName != "" &&
			data?.userInfoSection?.dateOfBirth != 0L &&
			data?.userInfoSection?.address != ""
}
