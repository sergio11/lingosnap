package com.dreamsoftware.lingosnap.ui.screens.home

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.HomeErrorMapper
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.usecase.DeleteOutfitByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAllOutfitsByUserUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SearchOutfitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllOutfitsByUserUseCase: GetAllOutfitsByUserUseCase,
    private val deleteOutfitByIdUseCase: DeleteOutfitByIdUseCase,
    private val searchOutfitUseCase: SearchOutfitUseCase,
    @HomeErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<HomeUiState, HomeSideEffects>(), HomeScreenActionListener {

    fun loadData() {
        onLoadData()
    }

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    override fun onOutfitClicked(outfitBO: OutfitBO) {
        launchSideEffect(HomeSideEffects.OpenOutfitChat(outfitBO.uid))
    }

    override fun onOutfitDetailClicked(outfitBO: OutfitBO) {
        launchSideEffect(HomeSideEffects.OpenOutfitDetail(outfitBO.uid))
    }

    override fun onSearchQueryUpdated(newSearchQuery: String) {
        updateState { it.copy(searchQuery = newSearchQuery) }
        onLoadData()
    }

    override fun onOutfitDeleted(outfitBO: OutfitBO) {
        updateState { it.copy(confirmDeleteOutfit = outfitBO) }
    }

    override fun onDeleteOutfitConfirmed() {
        doOnUiState {
            confirmDeleteOutfit?.let { outfit ->
                executeUseCaseWithParams(
                    useCase = deleteOutfitByIdUseCase,
                    params = DeleteOutfitByIdUseCase.Params(
                        id = outfit.uid
                    ),
                    onSuccess = {
                        onDeleteOutfitCompleted(outfit)
                    },
                    onMapExceptionToState = ::onMapExceptionToState
                )
            }
            updateState { it.copy(confirmDeleteOutfit = null) }
        }
    }

    override fun onDeleteOutfitCancelled() {
        updateState { it.copy(confirmDeleteOutfit = null) }
    }

    override fun onInfoMessageCleared() {
        updateState { it.copy(infoMessage = null) }
    }

    private fun onDeleteOutfitCompleted(outfit: OutfitBO) {
        updateState { it.copy(outfitList = it.outfitList.filter { iq -> iq.uid != outfit.uid }) }
    }

    private fun onLoadOutfitCompleted(data: List<OutfitBO>) {
        updateState {
            it.copy(outfitList = data)
        }
    }

    private fun onLoadData() {
        doOnUiState {
            if(searchQuery.isEmpty()) {
                executeUseCase(
                    useCase = getAllOutfitsByUserUseCase,
                    onSuccess = ::onLoadOutfitCompleted,
                    onMapExceptionToState = ::onMapExceptionToState
                )
            } else {
                executeUseCaseWithParams(
                    useCase = searchOutfitUseCase,
                    params = SearchOutfitUseCase.Params(term = searchQuery),
                    onSuccess = ::onLoadOutfitCompleted,
                    onMapExceptionToState = ::onMapExceptionToState
                )
            }
        }
    }

    private fun onMapExceptionToState(ex: Exception, uiState: HomeUiState) =
        uiState.copy(
            isLoading = false,
            errorMessage = errorMapper.mapToMessage(ex)
        )
}

data class HomeUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val confirmDeleteOutfit: OutfitBO? = null,
    val infoMessage: String? = null,
    val outfitList: List<OutfitBO> = emptyList(),
    val searchQuery: String = String.EMPTY
): UiState<HomeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): HomeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface HomeSideEffects: SideEffect {
    data class OpenOutfitDetail(val id: String): HomeSideEffects
    data class OpenOutfitChat(val id: String): HomeSideEffects
}