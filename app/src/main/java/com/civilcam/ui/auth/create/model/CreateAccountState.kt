package com.civilcam.ui.auth.create.model

import com.civilcam.common.ext.compose.ComposeFragmentState

data class CreateAccountState(
	val isLoading: Boolean = false,
	val errorText: String = "",
) : ComposeFragmentState