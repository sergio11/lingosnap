package com.dreamsoftware.lingosnap.data.remote.dto

import com.google.firebase.Timestamp

data class LingoSnapDTO(
    val uid: String,
    val userId: String,
    val imageUrl: String,
    val imageDescription: String,
    val createAt: Timestamp,
    val messages: List<LingoSnapMessageDTO>
)