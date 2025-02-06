package com.dreamsoftware.lingosnap.ui.screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.ui.utils.shareApp

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { SettingsUiState() },
        onSideEffect = {
            when (it) {
                SettingsUiSideEffects.ShareApp -> context.shareApp(R.string.share_message)
            }
        },
        onInit = {
            onInit()
        }
    ) { uiState ->
        SettingsScreenContent(
            uiState = uiState,
            actionListener = viewModel
        )
    }
}