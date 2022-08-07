package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domain.PictureModel
import com.civilcam.domain.model.CurrentUser
import com.civilcam.domain.model.SearchModel
import com.civilcam.domain.model.UserBaseInfo
import com.civilcam.domain.model.UserSetupModel

data class UserProfileState(
	val isLoading: Boolean = false,
	var errorText: String = "",
	var data: CurrentUser? = null,
	val user: UserSetupModel? = null,
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
