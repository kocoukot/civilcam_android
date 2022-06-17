package com.civilcam.ui.common.alert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.civilcam.common.theme.CCTheme

@Composable
fun AlertDialogComp(
    dialogTitle: String,
    dialogText: String,
    alertType: AlertTypes,
    onOptionSelected: (Boolean) -> Unit
) {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {
                AlertDialog(
                    properties = DialogProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false
                    ),
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(
                            text = dialogTitle,
                            color = CCTheme.colors.black,
                            style = CCTheme.typography.common_text_small_medium,
                            fontSize = 16.sp
                        )
                    },
                    text = {
                        Text(
                            dialogText,
                            color = CCTheme.colors.grayText,
                            style = CCTheme.typography.common_text_small_regular,
                            fontSize = 15.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            border = BorderStroke(0.dp, CCTheme.colors.white),
                            elevation = ButtonDefaults.elevation(0.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                            onClick = {
                                onOptionSelected(true)
                                openDialog.value = false
                            }) {
                            Text(
                                text = alertType.positiveText,
                                style = CCTheme.typography.common_text_medium,
                                fontSize = 14.sp,
                                color = CCTheme.colors.primaryRed
                            )

                        }
                    },


                    dismissButton = {
                        if (alertType == AlertTypes.OK || alertType == AlertTypes.GOT_IT || alertType == AlertTypes.GREAT) {

                        } else {
                            Button(
                                border = BorderStroke(0.dp, CCTheme.colors.white),
                                elevation = ButtonDefaults.elevation(0.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                onClick = {
                                    onOptionSelected(false)
                                    openDialog.value = false
                                }
                            ) {
                                Text(
                                    text = alertType.negativeText,
                                    style = CCTheme.typography.common_text_medium,
                                    fontSize = 14.sp,
                                    color = CCTheme.colors.primaryRed
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}

