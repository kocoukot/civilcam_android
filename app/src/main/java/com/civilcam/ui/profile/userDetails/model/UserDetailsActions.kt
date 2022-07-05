package com.civilcam.ui.profile.userDetails.model

sealed class UserDetailsActions {
    object ClickGoBack : UserDetailsActions()
    object ClickGuardenceChange : UserDetailsActions()
    data class ClickShowAlert(val alertType: StopGuardAlertType) : UserDetailsActions()
    object ClickCloseAlert : UserDetailsActions()

    data class ClickRequestAnswer(val isAccepted: Boolean) : UserDetailsActions()
    object ClickStopGuarding : UserDetailsActions()

    object Mock : UserDetailsActions()

}
