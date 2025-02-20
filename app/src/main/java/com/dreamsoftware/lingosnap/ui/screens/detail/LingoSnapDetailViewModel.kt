package com.dreamsoftware.lingosnap.ui.screens.detail

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.LingoSnapDetailErrorMapper
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.usecase.DeleteLingoSnapByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetLingoSnapByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LingoSnapDetailViewModel @Inject constructor(
    private val getLingoSnapByIdUseCase: GetLingoSnapByIdUseCase,
    private val deleteLingoSnapByIdUseCase: DeleteLingoSnapByIdUseCase,
    @LingoSnapDetailErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<LingoSnapDetailUiState, LingoSnapDetailSideEffects>(), LingoSnapDetailScreenActionListener {

    fun load(id: String) {
        executeUseCaseWithParams(
            useCase = getLingoSnapByIdUseCase,
            params = GetLingoSnapByIdUseCase.Params(id = id),
            onSuccess = ::onLoadLingoSnapDetailCompleted,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onGetDefaultState(): LingoSnapDetailUiState = LingoSnapDetailUiState()

    private fun onLoadLingoSnapDetailCompleted(lingoSnapBO: LingoSnapBO) {
        updateState {
            with(lingoSnapBO) {
                it.copy(
                    uid = uid,
                    imageUrl = imageUrl,
                    title = question,
                    description = imageDescription
                )
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: LingoSnapDetailUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )

    override fun onBackPressed() {
        launchSideEffect(LingoSnapDetailSideEffects.CloseDetail)
    }

    override fun onOpenChatClicked() {
        launchSideEffect(LingoSnapDetailSideEffects.OpenLingoSnapChat)
    }

    override fun onLingoSnapDeleted() {
        updateState { it.copy(showDeleteDialog = true) }
    }

    override fun onDeleteConfirmed() {
        updateState { it.copy(showDeleteDialog = false) }
        executeUseCaseWithParams(
            useCase = deleteLingoSnapByIdUseCase,
            params = DeleteLingoSnapByIdUseCase.Params(id = uiState.value.uid),
            onSuccess = { onDeleteLingoSnapCompleted() },
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onDeleteCancelled() {
        updateState { it.copy(showDeleteDialog = false) }
    }

    private fun onDeleteLingoSnapCompleted() {
        launchSideEffect(LingoSnapDetailSideEffects.LingoSnapDeleted)
    }
}

data class LingoSnapDetailUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val infoMessage: String = String.EMPTY,
    val uid: String = String.EMPTY,
    val imageUrl: String = String.EMPTY,
    val title: String = String.EMPTY,
    val description: String = String.EMPTY
) : UiState<LingoSnapDetailUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): LingoSnapDetailUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface LingoSnapDetailSideEffects : SideEffect {
    data object CloseDetail: LingoSnapDetailSideEffects
    data object LingoSnapDeleted: LingoSnapDetailSideEffects
    data object OpenLingoSnapChat: LingoSnapDetailSideEffects
}