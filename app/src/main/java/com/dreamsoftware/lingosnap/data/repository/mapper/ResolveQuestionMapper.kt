package com.dreamsoftware.lingosnap.data.repository.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.QuestionHistoryMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO

internal class ResolveQuestionMapper : IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO> {
    override fun mapInListToOutList(input: Iterable<ResolveQuestionBO>): Iterable<ResolveQuestionDTO> =
        input.map(::mapInToOut)

    override fun mapInToOut(input: ResolveQuestionBO): ResolveQuestionDTO = with(input) {
        ResolveQuestionDTO(
            question = question,
            context = context,
            history = history.map {
                QuestionHistoryMessageDTO(
                    role = it.first,
                    text = it.second
                )
            }
        )
    }
}