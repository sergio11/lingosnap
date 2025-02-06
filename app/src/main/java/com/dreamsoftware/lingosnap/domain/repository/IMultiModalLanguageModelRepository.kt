package com.dreamsoftware.lingosnap.domain.repository

import com.dreamsoftware.lingosnap.domain.exception.GenerateImageDescriptionException
import com.dreamsoftware.lingosnap.domain.exception.ResolveQuestionFromContextException
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO

interface IMultiModalLanguageModelRepository {

    @Throws(ResolveQuestionFromContextException::class)
    suspend fun resolveQuestion(data: ResolveQuestionBO): String

    @Throws(GenerateImageDescriptionException::class)
    suspend fun generateImageDescription(imageUrl: String): String
}