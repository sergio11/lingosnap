package com.dreamsoftware.lingosnap.domain.model

import java.util.Date

data class LingoSnapBO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val createAt: Date,
    val question: String,
    val messages: List<LingoSnapMessageBO>
)
