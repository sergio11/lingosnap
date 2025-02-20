package com.dreamsoftware.lingosnap.ui.screens.home

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import com.dreamsoftware.brownie.utils.EMPTY
import com.dreamsoftware.lingosnap.di.HomeErrorMapper
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.usecase.DeleteLingoSnapByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAllLingoSnapsByUserUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SearchLingoSnapUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllLingoSnapsByUserUseCase: GetAllLingoSnapsByUserUseCase,
    private val deleteLingoSnapByIdUseCase: DeleteLingoSnapByIdUseCase,
    private val searchLingoSnapUseCase: SearchLingoSnapUseCase,
    @HomeErrorMapper private val errorMapper: IBrownieErrorMapper
) : BrownieViewModel<HomeUiState, HomeSideEffects>(), HomeScreenActionListener {

    fun loadData() {
        onLoadData()
    }

    override fun onGetDefaultState(): HomeUiState = HomeUiState()

    override fun onLingoSnapClicked(lingoSnapBO: LingoSnapBO) {
        launchSideEffect(HomeSideEffects.OpenLingoSnapChat(lingoSnapBO.uid))
    }

    override fun onLingoSnapDetailClicked(lingoSnapBO: LingoSnapBO) {
        launchSideEffect(HomeSideEffects.OpenLingoSnapDetail(lingoSnapBO.uid))
    }

    override fun onSearchQueryUpdated(newSearchQuery: String) {
        updateState { it.copy(searchQuery = newSearchQuery) }
        onLoadData()
    }

    override fun onLingoSnapDeleted(lingoSnapBO: LingoSnapBO) {
        updateState { it.copy(confirmDeleteLingoSnap = lingoSnapBO) }
    }

    override fun onDeleteLingoSnapConfirmed() {
        doOnUiState {
            confirmDeleteLingoSnap?.let { outfit ->
                executeUseCaseWithParams(
                    useCase = deleteLingoSnapByIdUseCase,
                    params = DeleteLingoSnapByIdUseCase.Params(
                        id = outfit.uid
                    ),
                    onSuccess = {
                        onDeleteLingoSnapCompleted(outfit)
                    },
                    onMapExceptionToState = ::onMapExceptionToState
                )
            }
            updateState { it.copy(confirmDeleteLingoSnap = null) }
        }
    }

    override fun onDeleteLingoSnapCancelled() {
        updateState { it.copy(confirmDeleteLingoSnap = null) }
    }

    override fun onInfoMessageCleared() {
        updateState { it.copy(infoMessage = null) }
    }

    private fun onDeleteLingoSnapCompleted(outfit: LingoSnapBO) {
        updateState { it.copy(lingoSnapList = it.lingoSnapList.filter { iq -> iq.uid != outfit.uid }) }
    }

    private fun onLoadLingoSnapCompleted(data: List<LingoSnapBO>) {
        updateState {
            it.copy(lingoSnapList = data)
        }
    }

    private fun onLoadData() {
        doOnUiState {
            if (searchQuery.isEmpty()) {
                executeUseCase(
                    useCase = getAllLingoSnapsByUserUseCase,
                    onSuccess = ::onLoadLingoSnapCompleted,
                    onMapExceptionToState = ::onMapExceptionToState
                )
            } else {
                executeUseCaseWithParams(
                    useCase = searchLingoSnapUseCase,
                    params = SearchLingoSnapUseCase.Params(term = searchQuery),
                    onSuccess = ::onLoadLingoSnapCompleted,
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
    val confirmDeleteLingoSnap: LingoSnapBO? = null,
    val infoMessage: String? = null,
    val lingoSnapList: List<LingoSnapBO> = emptyList(),
    val searchQuery: String = String.EMPTY
) : UiState<HomeUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): HomeUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}


sealed interface HomeSideEffects : SideEffect {
    data class OpenLingoSnapDetail(val id: String) : HomeSideEffects
    data class OpenLingoSnapChat(val id: String) : HomeSideEffects
}