package com.dreamsoftware.lingosnap.ui.screens.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

data class OutfitDetailScreenArgs(
    val id: String
)

@Composable
fun OutfitDetailScreen(
    viewModel: OutfitDetailViewModel = hiltViewModel(),
    args: OutfitDetailScreenArgs,
    onGoToChat: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { OutfitDetailUiState() },
        onSideEffect = {
            when(it) {
                OutfitDetailSideEffects.CloseDetail -> onBackPressed()
                OutfitDetailSideEffects.OutfitDeleted -> onBackPressed()
                OutfitDetailSideEffects.OpenOutfitChat -> onGoToChat(args.id)
            }
        },
        onInit = { load(id = args.id) }
    ) { uiState ->
        OutfitDetailScreenContent(
            context = context,
            density = density,
            scrollState = scrollState,
            uiState = uiState,
            actionListener = viewModel
        )
    }
}
