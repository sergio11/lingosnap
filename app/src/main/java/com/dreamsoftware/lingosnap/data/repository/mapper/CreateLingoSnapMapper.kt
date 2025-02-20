package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.CreateLingoSnapDTO
import com.dreamsoftware.lingosnap.domain.model.CreateLingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapMessageRoleEnum

internal class CreateLingoSnapMapper: IBrownieOneSideMapper<CreateLingoSnapBO, CreateLingoSnapDTO> {

    override fun mapInListToOutList(input: Iterable<CreateLingoSnapBO>): Iterable<CreateLingoSnapDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: CreateLingoSnapBO): CreateLingoSnapDTO = with(input) {
        CreateLingoSnapDTO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            imageDescription = imageDescription,
            question = question,
            questionRole = LingoSnapMessageRoleEnum.USER.name,
            answer = answer,
            answerRole = LingoSnapMessageRoleEnum.MODEL.name
        )
    }
}