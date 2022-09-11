package com.civilcam.ui.common.alert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.ext_features.theme.CCTheme

@Composable
fun AlertDialogComp(
    dialogTitle: String = "",
    dialogText: String,
    alertType: AlertDialogTypes,
    onOptionSelected: (Boolean) -> Unit
) {
    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(true) }
            if (openDialog.value) {
                if (dialogTitle.isNotEmpty())
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
                                color = CCTheme.colors.grayOne,
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
                                    text = stringResource(id = alertType.positiveText),
                                    style = CCTheme.typography.common_text_medium,
                                    fontSize = 14.sp,
                                    color = CCTheme.colors.cianColor
                                )

                            }
                        },

                        dismissButton = {
                            if (alertType == AlertDialogTypes.OK //||
//                            alertType == AlertDialogTypes.GOT_IT ||
//                            alertType == AlertDialogTypes.GREAT
                            ) {

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

                                        text = stringResource(id = alertType.negativeText),
                                        style = CCTheme.typography.common_text_medium,
                                        fontSize = 14.sp,
                                        color = CCTheme.colors.cianColor
                                    )
                                }
                            }
                        }
                    )
                else
                    AlertDialog(
                        properties = DialogProperties(
                            dismissOnBackPress = false,
                            dismissOnClickOutside = false
                        ),
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        text = {
                            Text(
                                dialogText,
                                color = CCTheme.colors.grayOne,
                                style = CCTheme.typography.common_text_small_regular,
                                fontSize = 16.sp,
                                lineHeight = 24.sp
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
                                    text = stringResource(id = alertType.positiveText),
                                    style = CCTheme.typography.common_text_medium,
                                    fontSize = 14.sp,
                                    color = CCTheme.colors.cianColor
                                )

                            }
                        },

                        dismissButton = {
                            if (alertType == AlertDialogTypes.OK //||
//                            alertType == AlertDialogTypes.GOT_IT ||
//                            alertType == AlertDialogTypes.GREAT
                            ) {

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

                                        text = stringResource(id = alertType.negativeText),
                                        style = CCTheme.typography.common_text_medium,
                                        fontSize = 14.sp,
                                        color = CCTheme.colors.cianColor
                                    )
                                }
                            }
                        }
                    )
            }
        }
    }
}

