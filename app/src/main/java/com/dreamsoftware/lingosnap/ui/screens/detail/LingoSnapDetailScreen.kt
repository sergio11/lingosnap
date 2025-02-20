package com.dreamsoftware.lingosnap.ui.screens.detail

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen

data class LingoSnapDetailScreenArgs(
    val id: String
)

@Composable
fun LingoSnapDetailScreen(
    viewModel: LingoSnapDetailViewModel = hiltViewModel(),
    args: LingoSnapDetailScreenArgs,
    onGoToChat: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val scrollState: ScrollState = rememberScrollState(0)
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { LingoSnapDetailUiState() },
        onSideEffect = {
            when(it) {
                LingoSnapDetailSideEffects.CloseDetail -> onBackPressed()
                LingoSnapDetailSideEffects.LingoSnapDeleted -> onBackPressed()
                LingoSnapDetailSideEffects.OpenLingoSnapChat -> onGoToChat(args.id)
            }
        },
        onInit = { load(id = args.id) }
    ) { uiState ->
        LingoSnapDetailScreenContent(
            context = context,
            density = density,
            scrollState = scrollState,
            uiState = uiState,
            actionListener = viewModel
        )
    }
}
