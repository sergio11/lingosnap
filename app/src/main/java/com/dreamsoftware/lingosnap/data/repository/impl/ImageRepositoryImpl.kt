package com.dreamsoftware.lingosnap.data.repository.impl

import com.dreamsoftware.lingosnap.data.remote.datasource.IImageDataSource
import com.dreamsoftware.lingosnap.data.remote.exception.DeletePictureRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.SavePictureRemoteDataException
import com.dreamsoftware.lingosnap.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.lingosnap.domain.exception.DeletePictureException
import com.dreamsoftware.lingosnap.domain.exception.SavePictureException
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class ImageRepositoryImpl(
    private val imageDataSource: IImageDataSource,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), IImageRepository {

    @Throws(SavePictureException::class)
    override suspend fun save(path: String, name: String): String = safeExecute {
       try {
           imageDataSource.save(path, name)
       } catch (ex: SavePictureRemoteDataException) {
           throw SavePictureException("An error occurred when trying to save the picture", ex)
       }
    }

    override suspend fun deleteByName(name: String) = safeExecute {
        try {
            imageDataSource.deleteByName(name)
        } catch (ex: DeletePictureRemoteDataException) {
            throw DeletePictureException("An error occurred when trying to delete the picture", ex)
        }
    }
}