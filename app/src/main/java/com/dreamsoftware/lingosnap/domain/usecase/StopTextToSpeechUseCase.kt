package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCase
import com.dreamsoftware.lingosnap.domain.service.ITTSService

class StopTextToSpeechUseCase(
    private val ttsService: ITTSService
) : BrownieUseCase<Unit>() {

    override suspend fun onExecuted() {
        ttsService.stop()
    }
}