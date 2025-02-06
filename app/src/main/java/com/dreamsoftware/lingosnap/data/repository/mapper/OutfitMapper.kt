package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitDTO
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.model.OutfitMessageBO
import com.dreamsoftware.lingosnap.domain.model.OutfitMessageRoleEnum
import com.dreamsoftware.lingosnap.utils.enumNameOfOrDefault

internal class OutfitMapper : IBrownieOneSideMapper<OutfitDTO, OutfitBO> {

    override fun mapInListToOutList(input: Iterable<OutfitDTO>): Iterable<OutfitBO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: OutfitDTO): OutfitBO = with(input) {
        OutfitBO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            imageDescription = imageDescription,
            createAt = createAt.toDate(),
            question = messages.first().text,
            messages = messages.map {
                OutfitMessageBO(
                    uid = it.uid,
                    role = enumNameOfOrDefault(it.role, OutfitMessageRoleEnum.MODEL),
                    text = it.text
                )
            }
        )
    }
}