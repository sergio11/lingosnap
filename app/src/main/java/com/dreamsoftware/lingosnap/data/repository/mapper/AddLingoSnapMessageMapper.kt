package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapMessageRoleEnum

internal class AddLingoSnapMessageMapper : IBrownieOneSideMapper<AddMessageBO, AddMessageDTO> {

    override fun mapInListToOutList(input: Iterable<AddMessageBO>): Iterable<AddMessageDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: AddMessageBO): AddMessageDTO = with(input) {
        AddMessageDTO(
            uid = uid,
            userId = userId,
            question = question,
            questionRole = LingoSnapMessageRoleEnum.USER.name,
            answer = answer,
            answerRole = LingoSnapMessageRoleEnum.MODEL.name
        )
    }
}