package com.dreamsoftware.lingosnap.ui.screens.create

import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.dreamsoftware.brownie.component.screen.BrownieScreen
import com.dreamsoftware.lingosnap.utils.takePicture

@Composable
fun CreateLingoSnapScreen(
    viewModel: CreateLingoSnapViewModel = hiltViewModel(),
    onGoToChat: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    BrownieScreen(
        viewModel = viewModel,
        onBackPressed = onBackPressed,
        onInitialUiState = { CreateLingoSnapUiState() },
        onSideEffect = {
            when(it) {
                CreateLingoSnapSideEffects.StartListening -> {
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
                is CreateLingoSnapSideEffects.LingoSnapCreated -> onGoToChat(it.id)
            }
        }
    ) { uiState ->
        CreateLingoSnapScreenContent(
            uiState = uiState,
            actionListener = viewModel,
            lifecycleCameraController = cameraController
        )
    }
}
