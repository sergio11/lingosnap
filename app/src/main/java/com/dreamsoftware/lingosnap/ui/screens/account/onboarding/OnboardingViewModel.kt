package com.dreamsoftware.lingosnap.ui.screens.account.onboarding

import com.dreamsoftware.brownie.core.BrownieViewModel
import com.dreamsoftware.brownie.core.SideEffect
import com.dreamsoftware.brownie.core.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() :
    BrownieViewModel<OnboardingUiState, OnboardingSideEffects>(), OnboardingScreenActionListener {
    override fun onGetDefaultState(): OnboardingUiState = OnboardingUiState()
    override fun onNavigateToSignIn() {
        launchSideEffect(OnboardingSideEffects.NavigateToSignIn)
    }

    override fun onNavigateToSignUp() {
        launchSideEffect(OnboardingSideEffects.NavigateToSignUp)
    }
}

data class OnboardingUiState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : UiState<OnboardingUiState>(isLoading, errorMessage) {
    override fun copyState(isLoading: Boolean, errorMessage: String?): OnboardingUiState =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

sealed interface OnboardingSideEffects : SideEffect {
    data object NoAuthenticated : OnboardingSideEffects
    data object UserAlreadyAuthenticated : OnboardingSideEffects
    data object NavigateToSignIn : OnboardingSideEffects
    data object NavigateToSignUp : OnboardingSideEffects
}