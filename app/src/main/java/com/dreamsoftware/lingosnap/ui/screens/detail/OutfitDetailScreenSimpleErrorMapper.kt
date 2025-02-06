package com.dreamsoftware.lingosnap.ui.screens.detail

import android.content.Context
import com.dreamsoftware.brownie.core.IBrownieErrorMapper
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.domain.exception.InvalidDataException

class OutfitDetailScreenSimpleErrorMapper(
    private val context: Context
): IBrownieErrorMapper {
    override fun mapToMessage(ex: Throwable): String = context.getString(when(ex) {
        is InvalidDataException -> R.string.generic_form_invalid_data_provided
        else -> R.string.generic_error_exception
    })
}