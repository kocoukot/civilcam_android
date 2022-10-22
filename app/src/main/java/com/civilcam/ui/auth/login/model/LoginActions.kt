package com.civilcam.ui.auth.login.model

import com.civilcam.domainLayer.model.profile.PasswordInputDataType
import com.civilcam.ext_features.compose.ComposeFragmentActions

sealed class LoginActions : ComposeFragmentActions {
    object ClickReset : LoginActions()
    object ClickLogin : LoginActions()
    object ClickRegister : LoginActions()
    object ClickBack : LoginActions()
    data class EnterInputData(val dataType: PasswordInputDataType, val data: String) :
        LoginActions()

    object ClearErrorText : LoginActions()

    object FBLogin : LoginActions()
    object GoogleLogin : LoginActions()


}