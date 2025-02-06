package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class GetOutfitByIdUseCase(
    private val userRepository: IUserRepository,
    private val outfitRepository: IOutfitRepository
) : BrownieUseCaseWithParams<GetOutfitByIdUseCase.Params, OutfitBO>() {

    override suspend fun onExecuted(params: Params): OutfitBO {
        val userId = userRepository.getUserAuthenticatedUid()
        return outfitRepository.fetchById(userId = userId, id = params.id)
    }

    data class Params(val id: String)
}