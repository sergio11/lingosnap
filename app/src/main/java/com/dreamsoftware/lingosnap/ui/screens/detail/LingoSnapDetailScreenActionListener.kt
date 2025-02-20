package com.dreamsoftware.lingosnap.ui.screens.detail

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener

interface LingoSnapDetailScreenActionListener: IBrownieScreenActionListener {
    fun onBackPressed()
    fun onOpenChatClicked()
    fun onLingoSnapDeleted()
    fun onDeleteConfirmed()
    fun onDeleteCancelled()
}