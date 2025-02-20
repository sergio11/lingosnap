package com.dreamsoftware.lingosnap.ui.screens.create

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface CreateLingoSnapScreenActionListener: IBrownieScreenActionListener {
    fun onStartListening()
    fun onUpdateQuestion(newQuestion: String)
    fun onCreate()
    fun onCancel()
}