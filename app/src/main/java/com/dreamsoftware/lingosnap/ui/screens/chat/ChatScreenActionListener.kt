package com.dreamsoftware.lingosnap.ui.screens.chat

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface ChatScreenActionListener: IBrownieScreenActionListener {

    fun onAssistantMutedChange(isMuted: Boolean)
    fun onAssistantSpeechStopped()
    fun onStartListening()
    fun onBackButtonClicked()
}