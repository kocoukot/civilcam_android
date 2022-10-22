package com.civilcam.ui.profile.credentials.model

import com.civilcam.ext_features.compose.ComposeFragmentActions
import com.civilcam.ui.profile.userProfile.model.UserProfileType

sealed class ChangeCredentialsActions : ComposeFragmentActions {
	data class ClickSave(val dataType: UserProfileType) : ChangeCredentialsActions()
	object ClickBack : ChangeCredentialsActions()
	object CheckCredential : ChangeCredentialsActions()
	data class EnteredEmail(val data: String) : ChangeCredentialsActions()
	data class EnteredPhone(val data: String) : ChangeCredentialsActions()
}