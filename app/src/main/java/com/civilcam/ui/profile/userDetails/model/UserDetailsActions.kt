package com.civilcam.ui.profile.userDetails.model

import com.civilcam.common.ext.compose.ComposeFragmentActions

sealed class UserDetailsActions : ComposeFragmentActions {
    object ClickGoBack : UserDetailsActions()
    object ClickGuardenceChange : UserDetailsActions()
    data class ClickShowAlert(val alertType: StopGuardAlertType) : UserDetailsActions()
    object ClickCloseAlert : UserDetailsActions()

    data class ClickRequestAnswer(val isAccepted: Boolean) : UserDetailsActions()
    object ClickStopGuarding : UserDetailsActions()
}
