package com.dreamsoftware.lingosnap.ui.screens.core

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.dreamsoftware.brownie.component.BrownieAsyncImage
import com.dreamsoftware.lingosnap.R

@Composable
fun CommonOutfitImage(
    modifier: Modifier,
    imageUrl: String
) {
    val context = LocalContext.current
    BrownieAsyncImage(
        modifier = modifier
            .aspectRatio(1f),
        context = context,
        imageUrl = imageUrl,
        defaultImagePlaceholderRes = R.drawable.ic_splash_placeholder
    )
}