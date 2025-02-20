package com.dreamsoftware.lingosnap.domain.model

data class LingoSnapMessageBO(
    val uid: String,
    val role: LingoSnapMessageRoleEnum,
    val text: String
)
