package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class GetAllLingoSnapsByUserUseCase(
    private val userRepository: IUserRepository,
    private val lingoSnapRepository: ILingoSnapRepository
) : BrownieUseCase<List<LingoSnapBO>>() {

    override suspend fun onExecuted(): List<LingoSnapBO> =
        lingoSnapRepository.fetchAllByUserId(userId = userRepository.getUserAuthenticatedUid())
}