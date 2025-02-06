package com.dreamsoftware.lingosnap.domain.repository

import com.dreamsoftware.lingosnap.domain.exception.DeletePictureException
import com.dreamsoftware.lingosnap.domain.exception.SavePictureException

interface IImageRepository {

    @Throws(SavePictureException::class)
    suspend fun save(path: String, name: String): String

    @Throws(DeletePictureException::class)
    suspend fun deleteByName(name: String)
}