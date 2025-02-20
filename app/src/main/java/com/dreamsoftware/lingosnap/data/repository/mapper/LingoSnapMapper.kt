package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.LingoSnapDTO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapMessageBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapMessageRoleEnum
import com.dreamsoftware.lingosnap.utils.enumNameOfOrDefault

internal class LingoSnapMapper : IBrownieOneSideMapper<LingoSnapDTO, LingoSnapBO> {

    override fun mapInListToOutList(input: Iterable<LingoSnapDTO>): Iterable<LingoSnapBO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: LingoSnapDTO): LingoSnapBO = with(input) {
        LingoSnapBO(
            uid = uid,
            userId = userId,
            imageUrl = imageUrl,
            imageDescription = imageDescription,
            createAt = createAt.toDate(),
            question = messages.first().text,
            messages = messages.map {
                LingoSnapMessageBO(
                    uid = it.uid,
                    role = enumNameOfOrDefault(it.role, LingoSnapMessageRoleEnum.MODEL),
                    text = it.text
                )
            }
        )
    }
}