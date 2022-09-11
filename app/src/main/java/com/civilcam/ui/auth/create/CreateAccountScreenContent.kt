package com.civilcam.ui.auth.create

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.civilcam.R
import com.civilcam.domainLayer.model.AlertDialogTypes
import com.civilcam.ext_features.compose.elements.*
import com.civilcam.ext_features.theme.CCTheme
import com.civilcam.ui.auth.create.model.CreateAccountActions
import com.civilcam.ui.auth.create.model.PasswordInputDataType
import com.civilcam.ui.auth.create.model.PasswordStrategyState
import com.civilcam.ui.common.alert.AlertDialogComp
import com.civilcam.ui.common.compose.inputs.PasswordStrategyBlocks

@Composable
fun CreateAccountScreenContent(viewModel: CreateAccountViewModel) {

    val state = viewModel.state.collectAsState()
    val checkedStrategies = remember { mutableStateOf(0) }
    val focusState = remember { mutableStateOf(false) }
    var passwordFocusState by remember { mutableStateOf(PasswordStrategyState.NONE) }
    var passwordHadFocus by remember { mutableStateOf(false) }

    if (state.value.isLoading) {
        DialogLoadingContent()
    }

    if (state.value.alertErrorText.isNotEmpty()) {
        AlertDialogComp(
            dialogText = state.value.alertErrorText,
            alertType = AlertDialogTypes.OK,
            onOptionSelected = { viewModel.setInputActions(CreateAccountActions.ClickOkAlert) })
    }
    Scaffold(
        backgroundColor = CCTheme.colors.white,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                TopAppBarContent(
                    title = stringResource(id = R.string.create_account),
                    navigationItem = {
                        BackButton {
                            viewModel.setInputActions(CreateAccountActions.ClickGoBack)
                        }
                    },
                )
                DividerLightGray()
            }
        }

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
//                .imePadding()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            EmailInputField(
                title = stringResource(id = R.string.create_account_email_label),
                text = state.value.email,
                placeHolder = stringResource(id = R.string.create_account_email_placeholder),
                hasError = !state.value.isEmail && !focusState.value,
                errorMessage = state.value.emailErrorText,
                onValueChanged = {
                    viewModel.setInputActions(
                        CreateAccountActions.EnterInputData(
                            PasswordInputDataType.EMAIL,
                            it
                        )
                    )
                },
                onFocusChanged = {
                    focusState.value = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PasswordField(
                name = stringResource(id = R.string.password),
                text = state.value.passwordModel.password,
                placeholder = stringResource(id = R.string.create_password),
                onValueChanged = {
                    viewModel.setInputActions(
                        CreateAccountActions.EnterInputData(
                            PasswordInputDataType.PASSWORD,
                            it
                        )
                    )
                },
                //hasError = checkedStrategies.value != 4,
                noMatch = state.value.passwordModel.noMatch,
                onFocusChanged = {
                    if (it) passwordHadFocus = true
                    if (passwordHadFocus) {
                        passwordFocusState =
                            if (!it) PasswordStrategyState.LOOSE_FOCUS else PasswordStrategyState.NONE
                    }

                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            PasswordStrategyBlocks(
                input = state.value.passwordModel.password,
                strategyUpdate = {
                    checkedStrategies.value = it
                },
                onLooseFocus = passwordFocusState
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordField(
                name = stringResource(id = R.string.confirm_password),
                text = state.value.passwordModel.confirmPassword,
                placeholder = stringResource(id = R.string.re_enter_password),
                onValueChanged = {
                    viewModel.setInputActions(
                        CreateAccountActions.EnterInputData(
                            PasswordInputDataType.PASSWORD_REPEAT,
                            it
                        )
                    )
                },
                noMatch = state.value.passwordModel.noMatch,
                isReEnter = state.value.passwordModel.noMatch,
                onFocusChanged = {
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            ComposeButton(
                title = stringResource(id = R.string.create_account),
                Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 40.dp),
                isActivated = state.value.isFilled,
                buttonClick = {
                    viewModel.setInputActions(
                        CreateAccountActions.ClickContinue
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                SocialImage(painterResource(id = R.drawable.ic_facebook)) {
                    viewModel.setInputActions(CreateAccountActions.FBLogin)
                }

                Spacer(modifier = Modifier.width(16.dp))

                SocialImage(painterResource(id = R.drawable.ic_google)) {
                    viewModel.setInputActions(CreateAccountActions.GoogleLogin)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    stringResource(id = R.string.already_have_account),
                    color = CCTheme.colors.grayOne
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    stringResource(id = R.string.log_in),
                    color = CCTheme.colors.primaryRed,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        viewModel.setInputActions(
                            CreateAccountActions.ClickLogin
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SocialImage(
    painter: Painter,
    onImageClick: () -> Unit
) {
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .clickable { onImageClick.invoke() }
    )
}
