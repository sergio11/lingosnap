package com.dreamsoftware.lingosnap.ui.screens.home

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO

interface HomeScreenActionListener: IBrownieScreenActionListener {
    fun onLingoSnapClicked(lingoSnapBO: LingoSnapBO)
    fun onLingoSnapDetailClicked(lingoSnapBO: LingoSnapBO)
    fun onSearchQueryUpdated(newSearchQuery: String)
    fun onLingoSnapDeleted(lingoSnapBO: LingoSnapBO)
    fun onDeleteLingoSnapConfirmed()
    fun onDeleteLingoSnapCancelled()
}