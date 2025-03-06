package com.dreamsoftware.lingosnap.ui.screens.create

import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieButton
import com.dreamsoftware.brownie.component.BrownieButtonStyleTypeEnum
import com.dreamsoftware.brownie.component.BrownieButtonTypeEnum
import com.dreamsoftware.brownie.component.BrownieDefaultTextField
import com.dreamsoftware.brownie.component.BrownieSheetSurface
import com.dreamsoftware.brownie.component.screen.BrownieScreenContent
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.ui.components.AnimatedMicButtonWithTranscript
import com.dreamsoftware.lingosnap.ui.components.CameraPreview
import com.dreamsoftware.lingosnap.ui.components.LoadingDialog
import com.dreamsoftware.lingosnap.ui.screens.core.CommonLingoSnapImage

@Composable
internal fun CreateLingoSnapScreenContent(
    uiState: CreateLingoSnapUiState,
    actionListener: CreateLingoSnapScreenActionListener,
    lifecycleCameraController: LifecycleCameraController
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        BrownieScreenContent(
            hasTopBar = false,
            errorMessage = errorMessage,
            infoMessage = infoMessage,
            screenContainerColor = MaterialTheme.colorScheme.primary,
            enableVerticalScroll = false,
            onInfoMessageCleared = actionListener::onInfoMessageCleared,
            onErrorMessageCleared = actionListener::onErrorMessageCleared,
        ) {
            if (showConfirm) {
                ConfirmLingoSnap(
                    uiState = uiState,
                    actionListener = actionListener,
                )
            } else {
                CreateLingoSnap(
                    uiState = uiState,
                    actionListener = actionListener,
                    lifecycleCameraController = lifecycleCameraController
                )
            }
        }
    }
}

@Composable
private fun CreateLingoSnap(
    uiState: CreateLingoSnapUiState,
    actionListener: CreateLingoSnapScreenActionListener,
    lifecycleCameraController: LifecycleCameraController
) {
    with(uiState) {
        Box {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                controller = lifecycleCameraController
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(0.4f))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.main_logo_inverse),
                    contentDescription = String.EMPTY,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            }
            AnimatedMicButtonWithTranscript(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp),
                userTextTranscription = question,
                isListening = isListening,
                onStartListening = actionListener::onStartListening
            )
        }
    }
}

@Composable
private fun ColumnScope.ConfirmLingoSnap(
    uiState: CreateLingoSnapUiState,
    actionListener: CreateLingoSnapScreenActionListener
) {
    with(uiState) {
        Image(
            painter = painterResource(id = R.drawable.main_logo_inverse),
            contentDescription = String.EMPTY,
            modifier = Modifier
                .height(90.dp)
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
        BrownieSheetSurface(
            enableVerticalScroll = true,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            CommonLingoSnapImage(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .width(250.dp)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                ,
                imageUrl = imageUrl
            )
            Spacer(modifier = Modifier.height(12.dp))
            BrownieDefaultTextField(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .height(200.dp),
                value = question,
                maxLines = 6,
                isSingleLine = false,
                labelRes = R.string.create_lingo_snap_question_text_field_label,
                placeHolderRes = R.string.create_lingo_snap_question_text_field_placeholder,
                leadingIconRes = R.drawable.icon_note,
                onValueChanged = actionListener::onUpdateQuestion,
                enableTextFieldSeparator = true
            )
            Spacer(modifier = Modifier.weight(1f))
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = actionListener::onCreate,
                textRes = R.string.create_lingo_snap_save_button_text
            )
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                style = BrownieButtonStyleTypeEnum.INVERSE,
                onClick = actionListener::onCancel,
                textColor = MaterialTheme.colorScheme.onSecondary,
                borderColor = MaterialTheme.colorScheme.secondary,
                textRes = R.string.create_lingo_snap_cancel_button_text
            )
        }
    }
}