package com.dreamsoftware.lingosnap.domain.model

data class OutfitMessageBO(
    val uid: String,
    val role: OutfitMessageRoleEnum,
    val text: String
)
