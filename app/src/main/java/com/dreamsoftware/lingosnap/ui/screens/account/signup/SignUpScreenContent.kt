package com.dreamsoftware.lingosnap.ui.screens.account.signup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieButton
import com.dreamsoftware.brownie.component.BrownieButtonStyleTypeEnum
import com.dreamsoftware.brownie.component.BrownieButtonTypeEnum
import com.dreamsoftware.brownie.component.BrownieDefaultTextField
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextFieldPassword
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.ui.screens.account.core.AccountScreen

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    actionListener: SignUpScreenActionListener
) {
    with(uiState) {
        with(MaterialTheme.colorScheme) {
            AccountScreen(
                mainTitleRes = R.string.signup_main_title_text,
                isLoading = isLoading,
                errorMessage = errorMessage,
                screenBackgroundRes = R.drawable.signup_background,
                onInfoMessageCleared = actionListener::onInfoMessageCleared,
                onErrorMessageCleared = actionListener::onErrorMessageCleared,
            ) {
                BrownieText(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 6.dp)
                        .fillMaxWidth(),
                    type = BrownieTextTypeEnum.TITLE_LARGE,
                    titleRes = R.string.signup_secondary_title_text,
                    textAlign = TextAlign.Center,
                    textBold = true
                )
                BrownieText(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 6.dp)
                        .fillMaxWidth(),
                    type = BrownieTextTypeEnum.TITLE_MEDIUM,
                    titleRes = R.string.signup_description_text,
                    textAlign = TextAlign.Center
                )
                BrownieDefaultTextField(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    labelRes = R.string.signup_input_email_label,
                    placeHolderRes = R.string.signup_input_email_placeholder,
                    keyboardType = KeyboardType.Email,
                    value = email,
                    errorMessage = emailError,
                    enableTextFieldSeparator = true,
                    leadingIconRes = R.drawable.icon_username,
                    onValueChanged = actionListener::onEmailChanged
                )
                BrownieTextFieldPassword(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    labelRes = R.string.signup_input_password_label,
                    placeHolderRes = R.string.signup_input_password_placeholder,
                    value = password,
                    errorMessage = passwordError,
                    enableTextFieldSeparator = true,
                    leadingIconRes = R.drawable.icon_secret,
                    onValueChanged = actionListener::onPasswordChanged
                )
                BrownieTextFieldPassword(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    labelRes = R.string.signup_input_confirm_password_label,
                    placeHolderRes = R.string.signup_input_confirm_password_placeholder,
                    value = confirmPassword,
                    enableTextFieldSeparator = true,
                    leadingIconRes = R.drawable.icon_secret,
                    onValueChanged = actionListener::onConfirmPasswordChanged
                )
                Spacer(modifier = Modifier.height(20.dp))
                BrownieButton(
                    modifier  = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textRes = R.string.signup_signup_button_text,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    type = BrownieButtonTypeEnum.LARGE,
                    onClick = actionListener::onSignUp
                )
                BrownieButton(
                    modifier  = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    textRes = R.string.signup_already_has_account_text,
                    type = BrownieButtonTypeEnum.LARGE,
                    style = BrownieButtonStyleTypeEnum.TRANSPARENT,
                    textColor = secondary,
                    borderColor = secondary,
                    onClick = actionListener::onNavigateToSignIn
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}