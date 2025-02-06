package com.dreamsoftware.lingosnap.ui.screens.create

import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.lingosnap.utils.takePicture

@Composable
fun CreateOutfitScreen(
    viewModel: CreateOutfitViewModel = hiltViewModel(),
    onGoToChat: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { CreateOutfitUiState() },
        onSideEffect = {
            when(it) {
                CreateOutfitSideEffects.StartListening -> {
                    cameraController.takePicture(
                        context = context,
                        onSuccess = {
                            onTranscribeUserQuestion(imageUrl = it)
                        },
                        onError = {
                            onCancelUserQuestion()
                        }
                    )
                }
                is CreateOutfitSideEffects.OutfitCreated -> onGoToChat(it.id)
            }
        }
    ) { uiState ->
        CreateOutfitScreenContent(
            uiState = uiState,
            actionListener = viewModel,
            lifecycleCameraController = cameraController
        )
    }
}
