package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class SearchOutfitUseCase(
    private val userRepository: IUserRepository,
    private val outfitRepository: IOutfitRepository,
) : BrownieUseCaseWithParams<SearchOutfitUseCase.Params, List<OutfitBO>>() {

    override suspend fun onExecuted(params: Params): List<OutfitBO> = with(params) {
        val userUid = userRepository.getUserAuthenticatedUid()
        outfitRepository.search(userUid, term)
    }

    data class Params(
        val term: String
    )
}