package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class GetLingoSnapByIdUseCase(
    private val userRepository: IUserRepository,
    private val lingoSnapRepository: ILingoSnapRepository
) : BrownieUseCaseWithParams<GetLingoSnapByIdUseCase.Params, LingoSnapBO>() {

    override suspend fun onExecuted(params: Params): LingoSnapBO {
        val userId = userRepository.getUserAuthenticatedUid()
        return lingoSnapRepository.fetchById(userId = userId, id = params.id)
    }

    data class Params(val id: String)
}