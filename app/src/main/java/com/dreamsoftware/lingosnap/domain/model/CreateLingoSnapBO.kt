package com.dreamsoftware.lingosnap.domain.model

data class CreateLingoSnapBO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val question: String,
    val answer: String
)
