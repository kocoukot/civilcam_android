package com.civilcam.ui.network.inviteByNumber

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civilcam.R
import com.civilcam.common.ext.formatToPhoneNumber
import com.civilcam.common.theme.CCTheme
import com.civilcam.ui.common.compose.BackButton
import com.civilcam.ui.common.compose.RowDivider
import com.civilcam.ui.common.compose.TextActionButton
import com.civilcam.ui.common.compose.TopAppBarContent
import com.civilcam.ui.common.compose.inputs.PhoneInputField
import com.civilcam.ui.network.inviteByNumber.model.InviteByNumberActions

@Composable
fun InviteByNumberScreenContent(viewModel: InviteByNumberViewModel) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    var number by remember { mutableStateOf("") }
    var isPhoneNumber by remember { mutableStateOf(false) }

    if (state.value.clearNumber != null) {
        isPhoneNumber = false
        number = ""
        viewModel.setInputActions(InviteByNumberActions.PhoneCleared)
    }

    Scaffold(
        backgroundColor = CCTheme.colors.white,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBarContent(
                    title = stringResource(id = R.string.invite_by_number_title),
                    actionItem = {
                        TextActionButton(
                            actionTitle = stringResource(id = R.string.invite_text),
                            isEnabled = isPhoneNumber
                        ) {
                            viewModel.setInputActions(InviteByNumberActions.SendInvite(number))
                        }
                    },
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(InviteByNumberActions.ClickGoBack)
                        }
                    },
                )
                RowDivider()
            }
        }) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(CCTheme.colors.white)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            PhoneInputField(
                text = number,
                isInFocus = { },
                onValueChanged = {
                    number = it
                    isPhoneNumber = it.length == 10
                })
            Text(
                text = stringResource(id = R.string.invite_by_number_hint),
                style = CCTheme.typography.common_text_small_regular,
                color = CCTheme.colors.grayOne,
                modifier = Modifier.padding(end = 8.dp)
            )

            state.value.data?.invitationList?.let { list ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(list) { number ->
                        InvitationRow(number, context)
                    }
                }
            }

        }
    }
}

@Composable
private fun InvitationRow(number: String, context: Context) {

    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = CCTheme.colors.black,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp
                ),
            ) {
                append("${context.resources.getString(R.string.invite_by_number_sent)} ")
            }
            withStyle(
                style = SpanStyle(
                    color = CCTheme.colors.primaryRed,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp
                ),
            ) {
                append(" ${number.formatToPhoneNumber()}")
            }

        },
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

 