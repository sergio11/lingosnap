package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class SignOffUseCase(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository
): BrownieUseCase<Unit>() {
    override suspend fun onExecuted() {
        userRepository.closeSession()
        preferenceRepository.clearData()
    }
}