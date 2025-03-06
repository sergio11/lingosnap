package com.dreamsoftware.lingosnap.ui.screens.chat

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.ChatErrorMapper
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapMessageBO
import com.dreamsoftware.lingosnap.domain.usecase.AddLingoSnapMessageUseCase
import com.dreamsoftware.lingosnap.domain.usecase.EndUserSpeechCaptureUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAssistantMutedStatusUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetLingoSnapByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.StopTextToSpeechUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TextToSpeechUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.lingosnap.domain.usecase.UpdateAssistantMutedStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getLingoSnapByIdUseCase: GetLingoSnapByIdUseCase,
    private val transcribeUserQuestionUseCase: TranscribeUserQuestionUseCase,
    private val endUserSpeechCaptureUseCase: EndUserSpeechCaptureUseCase,
    private val textToSpeechUseCase: TextToSpeechUseCase,
    private val stopTextToSpeechUseCase: StopTextToSpeechUseCase,
    private val addLingoSnapMessageUseCase: AddLingoSnapMessageUseCase,
    private val updateAssistantMutedStatusUseCase: UpdateAssistantMutedStatusUseCase,
    private val getAssistantMutedStatusUseCase: GetAssistantMutedStatusUseCase,
    @ChatErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<ChatUiState, ChatSideEffects>(), ChatScreenActionListener {

    fun load(id: String) {
        executeUseCase(
            useCase = getAssistantMutedStatusUseCase,
            onSuccess = ::onGetAssistantMutedStatusCompleted,
            showLoadingState = false
        )
        executeUseCaseWithParams(
            useCase = getLingoSnapByIdUseCase,
            params = GetLingoSnapByIdUseCase.Params(id = id),
            onSuccess = ::onGetLingoSnapCompletedSuccessfully,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onGetDefaultState(): ChatUiState = ChatUiState()

    override fun onAssistantMutedChange(isMuted: Boolean) {
        updateState { it.copy(isAssistantMuted = isMuted) }
        executeUseCaseWithParams(
            useCase = updateAssistantMutedStatusUseCase,
            params = UpdateAssistantMutedStatusUseCase.Params(isAssistantMuted = isMuted),
            showLoadingState = false
        )
        if (isMuted) {
            stopSpeaking()
        }
    }

    override fun onAssistantSpeechStopped() {
        if (uiState.value.isAssistantSpeaking) {
            stopSpeaking()
        }
    }

    override fun onStartListening() {
        if(uiState.value.isListening) {
            onStopTranscription()
        } else {
            onTranscribeUserQuestion()
        }
    }

    override fun onBackButtonClicked() {
        launchSideEffect(ChatSideEffects.CloseChat)
    }

    override fun onCleared() {
        doOnUiState {
            if(isAssistantSpeaking) {
                stopSpeaking()
            }
            if(isListening) {
                onStopTranscription()
            }
        }
        super.onCleared()
    }

    private fun onTranscribeUserQuestion() {
        updateState { it.copy(isListening = true) }
        executeUseCase(
            useCase = transcribeUserQuestionUseCase,
            onSuccess = ::onListenForTranscriptionCompleted,
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onStopTranscription() {
        executeUseCase(
            useCase = endUserSpeechCaptureUseCase,
            onSuccess = { updateState { it.copy(isListening = false) } },
            onMapExceptionToState = ::onMapExceptionToState,
            showLoadingState = false
        )
    }

    private fun onListenForTranscriptionCompleted(transcription: String) {
        updateState { it.copy(isListening = false) }
        executeUseCaseWithParams(
            useCase = addLingoSnapMessageUseCase,
            params = AddLingoSnapMessageUseCase.Params(
                lingoSnapId = uiState.value.lingoSnapId,
                question = transcription
            ),
            onSuccess = ::onGetLingoSnapCompletedSuccessfully,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    private fun onGetLingoSnapCompletedSuccessfully(lingoSnapBO: LingoSnapBO) {
        updateState { it.copy(lingoSnapId = lingoSnapBO.uid, messageList = lingoSnapBO.messages) }
        doOnUiState {
            if(!isAssistantMuted) {
                speakMessage(text = lingoSnapBO.messages.last().text)
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: ChatUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )

    private fun speakMessage(text: String) {
        updateState { it.copy(isAssistantSpeaking = true) }
        executeUseCaseWithParams(
            useCase = textToSpeechUseCase,
            params = TextToSpeechUseCase.Params(text),
            onMapExceptionToState = ::onMapExceptionToState,
            onSuccess = {
                updateState { it.copy(isAssistantSpeaking = false) }
            },
            showLoadingState = false
        )
    }

    private fun stopSpeaking() {
        executeUseCase(useCase = stopTextToSpeechUseCase)
        updateState { it.copy(isAssistantSpeaking = false) }
    }

    private fun onGetAssistantMutedStatusCompleted(isMuted: Boolean) {
        updateState { it.copy(isAssistantMuted = isMuted) }
    }
}


data class ChatUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val lingoSnapId: String = String.EMPTY,
    val infoMessage: String = String.EMPTY,
    val isAssistantResponseLoading: Boolean = false,
    val isAssistantMuted: Boolean = false,
    val isAssistantSpeaking: Boolean = false,
    val isListening: Boolean = false,
    val lastQuestion: String = String.EMPTY,
    val messageList: List<LingoSnapMessageBO> = emptyList()
): UiState<ChatUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): ChatUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface ChatSideEffects: SideEffect {
    data object CloseChat: ChatSideEffects
}

