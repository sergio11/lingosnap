package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

/**
 * VerifyUserSessionUseCase
 * @param userRepository
 */
class VerifyUserSessionUseCase(
    private val userRepository: IUserRepository
) : BrownieUseCase<AuthUserBO>() {

    override suspend fun onExecuted(): AuthUserBO = userRepository.getCurrentAuthenticatedUser()
}