package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.service.ITTSService

class TextToSpeechUseCase(
    private val ttsService: ITTSService
) : BrownieUseCaseWithParams<TextToSpeechUseCase.Params, Unit>() {

    override suspend fun onExecuted(params: Params) {
        ttsService.startSpeaking(params.text)
    }

    data class Params(
        val text: String
    )
}