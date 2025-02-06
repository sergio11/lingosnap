package com.dreamsoftware.lingosnap.ui.utils

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes

fun Context.shareApp(@StringRes shareMessageRes: Int) {
    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, getString(shareMessageRes))
    }, "Share app link"))
}