package com.civilcam.ui.profile.userDetails.model

import com.civilcam.common.ext.compose.ComposeFragmentActions
import com.civilcam.domainLayer.model.ButtonAnswer

sealed class UserDetailsActions : ComposeFragmentActions {
    object ClickGoBack : UserDetailsActions()
    object ClickGuardenceChange : UserDetailsActions()
    data class ClickShowAlert(val alertType: StopGuardAlertType) : UserDetailsActions()
    object ClickCloseAlert : UserDetailsActions()
    object ClickCloseErrorAlert : UserDetailsActions()

    data class ClickRequestAnswer(val isAccepted: ButtonAnswer) : UserDetailsActions()
    object ClickStopGuarding : UserDetailsActions()
}
