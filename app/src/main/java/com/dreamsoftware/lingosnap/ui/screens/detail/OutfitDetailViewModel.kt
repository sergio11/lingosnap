package com.dreamsoftware.lingosnap.ui.screens.detail

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.OutfitDetailErrorMapper
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.usecase.DeleteOutfitByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetOutfitByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OutfitDetailViewModel @Inject constructor(
    private val getOutfitByIdUseCase: GetOutfitByIdUseCase,
    private val deleteOutfitByIdUseCase: DeleteOutfitByIdUseCase,
    @OutfitDetailErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<OutfitDetailUiState, OutfitDetailSideEffects>(), OutfitDetailScreenActionListener {

    fun load(id: String) {
        executeUseCaseWithParams(
            useCase = getOutfitByIdUseCase,
            params = GetOutfitByIdUseCase.Params(id = id),
            onSuccess = ::onLoadOutfitDetailCompleted,
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onGetDefaultState(): OutfitDetailUiState = OutfitDetailUiState()

    private fun onLoadOutfitDetailCompleted(outfitBO: OutfitBO) {
        updateState {
            with(outfitBO) {
                it.copy(
                    uid = uid,
                    imageUrl = imageUrl,
                    title = question,
                    description = imageDescription
                )
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: OutfitDetailUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )

    override fun onBackPressed() {
        launchSideEffect(OutfitDetailSideEffects.CloseDetail)
    }

    override fun onOpenChatClicked() {
        launchSideEffect(OutfitDetailSideEffects.OpenOutfitChat)
    }

    override fun onOutfitDeleted() {
        updateState { it.copy(showDeleteDialog = true) }
    }

    override fun onDeleteConfirmed() {
        updateState { it.copy(showDeleteDialog = false) }
        executeUseCaseWithParams(
            useCase = deleteOutfitByIdUseCase,
            params = DeleteOutfitByIdUseCase.Params(id = uiState.value.uid),
            onSuccess = { onDeleteOutfitCompleted() },
            onMapExceptionToState = ::onMapExceptionToState
        )
    }

    override fun onDeleteCancelled() {
        updateState { it.copy(showDeleteDialog = false) }
    }

    private fun onDeleteOutfitCompleted() {
        launchSideEffect(OutfitDetailSideEffects.OutfitDeleted)
    }
}

data class OutfitDetailUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val showDeleteDialog: Boolean = false,
    val infoMessage: String = String.EMPTY,
    val uid: String = String.EMPTY,
    val imageUrl: String = String.EMPTY,
    val title: String = String.EMPTY,
    val description: String = String.EMPTY
) : UiState<OutfitDetailUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): OutfitDetailUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface OutfitDetailSideEffects : SideEffect {
    data object CloseDetail: OutfitDetailSideEffects
    data object OutfitDeleted: OutfitDetailSideEffects
    data object OpenOutfitChat: OutfitDetailSideEffects
}