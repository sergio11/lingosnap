package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.CreateOutfitDTO
import com.dreamsoftware.lingosnap.domain.model.CreateOutfitBO
import com.dreamsoftware.lingosnap.domain.model.OutfitMessageRoleEnum

internal class CreateOutfitMapper: IBrownieOneSideMapper<CreateOutfitBO, CreateOutfitDTO> {

    override fun mapInListToOutList(input: Iterable<CreateOutfitBO>): Iterable<CreateOutfitDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: CreateOutfitBO): CreateOutfitDTO = with(input) {
        CreateOutfitDTO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            imageDescription = imageDescription,
            question = question,
            questionRole = OutfitMessageRoleEnum.USER.name,
            answer = answer,
            answerRole = OutfitMessageRoleEnum.MODEL.name
        )
    }
}