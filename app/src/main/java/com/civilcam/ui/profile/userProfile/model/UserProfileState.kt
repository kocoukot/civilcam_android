package com.civilcam.ui.profile.userProfile.model

import com.civilcam.common.ext.compose.ComposeFragmentState
import com.civilcam.domainLayer.model.SearchModel
import com.civilcam.domainLayer.model.user.CurrentUser


data class UserProfileState(
    val isLoading: Boolean = false,
    var errorText: String = "",
    var data: CurrentUser? = null,
    var profileImage: String? = null,
    val showDatePicker: Boolean = false,
    val screenState: UserProfileScreen = UserProfileScreen.PROFILE,
    val searchLocationModel: SearchModel = SearchModel()
) : ComposeFragmentState {

    val isFilled = !data?.userBaseInfo?.firstName.isNullOrEmpty() ||
            !data?.userBaseInfo?.lastName.isNullOrEmpty() ||
            !data?.userBaseInfo?.dob.isNullOrEmpty() ||
            data?.userBaseInfo?.avatar != null ||
            data?.userBaseInfo?.address != null
}
