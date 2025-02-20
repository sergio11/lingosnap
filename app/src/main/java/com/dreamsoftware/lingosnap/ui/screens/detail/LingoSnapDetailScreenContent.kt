package com.dreamsoftware.lingosnap.ui.screens.detail

import android.content.Context
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.dreamsoftware.brownie.component.BrownieButton
import com.dreamsoftware.brownie.component.BrownieButtonStyleTypeEnum
import com.dreamsoftware.brownie.component.BrownieButtonTypeEnum
import com.dreamsoftware.brownie.component.BrownieDetailScreen
import com.dreamsoftware.brownie.component.BrownieDialog
import com.dreamsoftware.brownie.component.BrownieText
import com.dreamsoftware.brownie.component.BrownieTextTypeEnum
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.ui.components.LoadingDialog

@Composable
internal fun LingoSnapDetailScreenContent(
    context: Context,
    density: Density,
    scrollState: ScrollState,
    uiState: LingoSnapDetailUiState,
    actionListener: LingoSnapDetailScreenActionListener
) {
    with(uiState) {
        LoadingDialog(isShowingDialog = isLoading)
        BrownieDialog(
            isVisible = showDeleteDialog,
            mainLogoRes = R.drawable.main_logo,
            titleRes = R.string.delete_lingo_snap_dialog_title,
            descriptionRes = R.string.delete_lingo_snap_dialog_description,
            cancelRes = R.string.delete_lingo_snap_dialog_cancel,
            acceptRes = R.string.delete_lingo_snap_dialog_accept,
            onCancelClicked = actionListener::onDeleteCancelled,
            onAcceptClicked = actionListener::onDeleteConfirmed,
        )
        BrownieDetailScreen(
            context = context,
            errorMessage = errorMessage,
            infoMessage = infoMessage,
            isLoading = isLoading,
            density = density,
            imageUrl = imageUrl,
            title = title,
            scrollState = scrollState,
            defaultImagePlaceholderRes = R.drawable.ic_splash_placeholder,
            backIconRes = R.drawable.ic_back,
            onBackClicked = actionListener::onBackPressed
        ) {
            Spacer(modifier = Modifier.height(15.dp))
            BrownieText(
                type = BrownieTextTypeEnum.BODY_LARGE,
                titleRes = R.string.lingo_snap_detail_user_question_title,
                textBold = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            BrownieText(
                type = BrownieTextTypeEnum.BODY_MEDIUM,
                titleText = title
            )
            Spacer(modifier = Modifier.height(10.dp))
            BrownieText(
                type = BrownieTextTypeEnum.BODY_LARGE,
                titleRes = R.string.lingo_snap_detail_photo_context_title,
                textBold = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            BrownieText(
                type = BrownieTextTypeEnum.BODY_MEDIUM,
                titleText = description
            )
            Spacer(modifier = Modifier.height(30.dp))
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = actionListener::onOpenChatClicked,
                textRes = R.string.lingo_snap_detail_open_chat_text
            )
            BrownieButton(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                type = BrownieButtonTypeEnum.LARGE,
                textColor = MaterialTheme.colorScheme.secondary,
                borderColor = MaterialTheme.colorScheme.secondary,
                style = BrownieButtonStyleTypeEnum.TRANSPARENT,
                onClick = actionListener::onLingoSnapDeleted,
                textRes = R.string.lingo_snap_detail_delete_button_text
            )
        }
    }
}