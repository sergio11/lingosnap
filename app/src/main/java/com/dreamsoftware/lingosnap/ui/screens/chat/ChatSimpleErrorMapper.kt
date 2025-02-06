package com.dreamsoftware.lingosnap.ui.screens.chat

import android.content.Context
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.lingosnap.R

class ChatSimpleErrorMapper(
    private val context: Context
): IBrownieErrorMapper {
    override fun mapToMessage(ex: Throwable): String =
        context.getString(R.string.generic_error_exception)
}