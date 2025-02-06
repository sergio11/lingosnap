package com.dreamsoftware.lingosnap.ui.screens.create

import androidx.lifecycle.viewModelScope
import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.CreateOutfitErrorMapper
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.usecase.CreateOutfitUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.lingosnap.domain.usecase.EndUserSpeechCaptureUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateOutfitViewModel @Inject constructor(
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val createOutfitUseCase: CreateOutfitUseCase,
    @CreateOutfitErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<CreateOutfitUiState, CreateOutfitSideEffects>(), CreateOutfitScreenActionListener {

    companion object {
        private const val SHOW_CONFIRM_SCREEN_DELAY = 2000L
    }

    override fun onGetDefaultState(): CreateOutfitUiState = CreateOutfitUiState()

    fun onTranscribeUserQuestion(imageUrl: String) {
        updateState { it.copy(isListening = true, question = String.EMPTY, imageUrl = imageUrl) }
        executeUseCase(
            useCase = transcribeUserQuestionUseCase,
            onSuccess = ::onListenForTranscriptionCompleted,
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    fun onCancelUserQuestion() {
        onResetState()
    }

    override fun onStartListening() {
        if(uiState.value.isListening) {
            onStopTranscription()
        } else {
            launchSideEffect(CreateOutfitSideEffects.StartListening)
        }
    }

    override fun onUpdateQuestion(newQuestion: String) {
        updateState { it.copy(question = newQuestion) }
    }

    override fun onCreate() {
        with(uiState.value) {
            executeUseCaseWithParams(
                useCase = createOutfitUseCase,
                params = CreateOutfitUseCase.Params(imageUrl = imageUrl, question = question),
                onSuccess = ::onOutfitCreatedSuccessfully,
                onMapExceptionToState = ::onMapExceptionToState
            )
        }
    }

    override fun onCancel() {
        onResetState()
    }

    private fun onStopTranscription() {
        executeUseCase(
            useCase = endUserSpeechCaptureUseCase,
            onSuccess = { onResetState() },
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onListenForTranscriptionCompleted(transcription: String) {
        viewModelScope.launch {
            updateState { it.copy(isListening = false, question = transcription) }
            delay(SHOW_CONFIRM_SCREEN_DELAY)
            updateState { it.copy(showConfirm = true) }
        }
    }

    private fun onResetState() {
        updateState {
            it.copy(
                isListening = false,
                question = String.EMPTY,
                imageUrl = String.EMPTY,
                showConfirm = false
            )
        }
    }

    private fun onOutfitCreatedSuccessfully(data: OutfitBO) {
        onResetState()
        launchSideEffect(CreateOutfitSideEffects.OutfitCreated(data.uid))
    }

    private fun onMapExceptionToState(ex: Exception, uiState: CreateOutfitUiState) =
        uiState.copy(
            isLoading = false,
            isListening = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )
}

data class CreateOutfitUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showConfirm: Boolean = false,
    val infoMessage: String = String.EMPTY,
    val isListening: Boolean = false,
    val imageUrl: String = String.EMPTY,
    val question: String = String.EMPTY
) : UiState<CreateOutfitUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): CreateOutfitUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface CreateOutfitSideEffects : SideEffect {
    data object StartListening: CreateOutfitSideEffects
    data class OutfitCreated(val id: String): CreateOutfitSideEffects
}