package com.civilcam.ui.profile.userDetails.model

import androidx.compose.runtime.Composable
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.domainLayer.model.guard.PersonModel
import com.civilcam.ext_features.compose.ComposeFragmentState
import com.civilcam.ui.common.alert.AlertDialogComp

data class UserDetailsState(
    val isLoading: Boolean = false,
    val errorText: String = "",
    val data: PersonModel? = null,
    val alertType: StopGuardAlertType? = null
) : ComposeFragmentState {

    @Composable
    fun showAlert(alertAction: () -> Unit) = if (errorText.isNotEmpty()) AlertDialogComp(
        dialogText = errorText,
        alertType = AlertDialogTypes.OK,
        onOptionSelected = { alertAction.invoke() }
    ) else null
}