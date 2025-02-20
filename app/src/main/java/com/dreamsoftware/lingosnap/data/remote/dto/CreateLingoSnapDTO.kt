package com.dreamsoftware.lingosnap.data.remote.dto

data class CreateLingoSnapDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val question: String,
    val questionRole: String,
    val answer: String,
    val answerRole: String
)
