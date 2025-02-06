package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class GetAllOutfitsByUserUseCase(
    private val userRepository: IUserRepository,
    private val outfitRepository: IOutfitRepository
) : BrownieUseCase<List<OutfitBO>>() {

    override suspend fun onExecuted(): List<OutfitBO> =
        outfitRepository.fetchAllByUserId(userId = userRepository.getUserAuthenticatedUid())
}