package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

class DeleteOutfitByIdUseCase(
    private val userRepository: IUserRepository,
    private val imageRepository: IImageRepository,
    private val outfitRepository: IOutfitRepository
) : BrownieUseCaseWithParams<DeleteOutfitByIdUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params): Unit = with(params) {
        val userId = userRepository.getUserAuthenticatedUid()
        imageRepository.deleteByName(id)
        outfitRepository.deleteById(userId = userId, id = id)
    }

    data class Params(
        val id: String,
    )
}