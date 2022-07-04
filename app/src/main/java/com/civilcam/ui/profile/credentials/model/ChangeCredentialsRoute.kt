package com.civilcam.ui.profile.credentials.model

import com.civilcam.common.ext.compose.ComposeFragmentRoute

sealed class ChangeCredentialsRoute : ComposeFragmentRoute {
	data class GoSave(val dataType: CredentialType, val data: String) : ChangeCredentialsRoute()
}