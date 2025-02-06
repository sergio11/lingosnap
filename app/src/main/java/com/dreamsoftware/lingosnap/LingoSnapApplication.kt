package com.dreamsoftware.lingosnap

import android.app.Application
import com.dreamsoftware.brownie.utils.IBrownieAppEvent
import com.dreamsoftware.lingosnap.utils.IApplicationAware
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LingoSnapApplication : Application(), IApplicationAware

sealed interface AppEvent: IBrownieAppEvent {
    data object SignOff: AppEvent
}