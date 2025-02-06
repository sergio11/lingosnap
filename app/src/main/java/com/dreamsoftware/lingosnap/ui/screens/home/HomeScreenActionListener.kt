package com.dreamsoftware.lingosnap.ui.screens.home

import com.dreamsoftware.brownie.core.IBrownieScreenActionListener
import com.dreamsoftware.lingosnap.domain.model.OutfitBO

interface HomeScreenActionListener: IBrownieScreenActionListener {
    fun onOutfitClicked(outfitBO: OutfitBO)
    fun onOutfitDetailClicked(outfitBO: OutfitBO)
    fun onSearchQueryUpdated(newSearchQuery: String)
    fun onOutfitDeleted(outfitBO: OutfitBO)
    fun onDeleteOutfitConfirmed()
    fun onDeleteOutfitCancelled()
}