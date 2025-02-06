package com.dreamsoftware.lingosnap.domain.model

data class AuthUserBO(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String
)