package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository

class GetAssistantMutedStatusUseCase(
    private val preferencesRepository: IPreferenceRepository
) : BrownieUseCase<Boolean>() {

    override suspend fun onExecuted(): Boolean =
        preferencesRepository.isAssistantMuted()
}