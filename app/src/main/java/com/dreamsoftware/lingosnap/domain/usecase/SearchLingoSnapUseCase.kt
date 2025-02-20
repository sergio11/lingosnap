package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class SearchLingoSnapUseCase(
    private val userRepository: IUserRepository,
    private val lingoSnapRepository: ILingoSnapRepository,
) : BrownieUseCaseWithParams<SearchLingoSnapUseCase.Params, List<LingoSnapBO>>() {

    override suspend fun onExecuted(params: Params): List<LingoSnapBO> = with(params) {
        val userUid = userRepository.getUserAuthenticatedUid()
        lingoSnapRepository.search(userUid, term)
    }

    data class Params(
        val term: String
    )
}