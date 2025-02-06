package com.dreamsoftware.lingosnap.domain.model

data class AddMessageBO(
    val uid: String,
    val userId: String,
    val question: String,
    val answer: String
)
