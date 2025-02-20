package com.dreamsoftware.lingosnap.data.remote.datasource

import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateLingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.dto.LingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.exception.AddLingoSnapMessageRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.DeleteLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchAllLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.CreateLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.SearchLingoSnapRemoteDataException

interface ILingoSnapDataSource {

    @Throws(SearchLingoSnapRemoteDataException::class)
    suspend fun search(userId: String, term: String): List<LingoSnapDTO>

    @Throws(CreateLingoSnapRemoteDataException::class)
    suspend fun create(data: CreateLingoSnapDTO)

    @Throws(AddLingoSnapMessageRemoteDataException::class)
    suspend fun addMessage(data: AddMessageDTO)

    @Throws(FetchLingoSnapByIdRemoteDataException::class)
    suspend fun fetchById(userId: String, id: String): LingoSnapDTO

    @Throws(FetchAllLingoSnapRemoteDataException::class)
    suspend fun fetchAllByUserId(userId: String): List<LingoSnapDTO>

    @Throws(DeleteLingoSnapByIdRemoteDataException::class)
    suspend fun deleteById(userId: String, id: String)
}