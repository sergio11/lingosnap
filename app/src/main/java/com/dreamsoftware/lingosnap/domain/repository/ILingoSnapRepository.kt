package com.dreamsoftware.lingosnap.domain.repository

import com.dreamsoftware.lingosnap.domain.exception.AddLingoSnapMessageException
import com.dreamsoftware.lingosnap.domain.exception.DeleteLingoSnapByIdException
import com.dreamsoftware.lingosnap.domain.exception.FetchAllLingoSnapException
import com.dreamsoftware.lingosnap.domain.exception.FetchLingoSnapByIdException
import com.dreamsoftware.lingosnap.domain.exception.SaveLingoSnapException
import com.dreamsoftware.lingosnap.domain.exception.SearchLingoSnapException
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.CreateLingoSnapBO

interface ILingoSnapRepository {

    @Throws(SearchLingoSnapException::class)
    suspend fun search(userId: String, term: String): List<LingoSnapBO>

    @Throws(SaveLingoSnapException::class)
    suspend fun create(data: CreateLingoSnapBO): LingoSnapBO

    @Throws(AddLingoSnapMessageException::class)
    suspend fun addMessage(data: AddMessageBO): LingoSnapBO

    @Throws(DeleteLingoSnapByIdException::class)
    suspend fun deleteById(userId: String, id: String)

    @Throws(FetchLingoSnapByIdException::class)
    suspend fun fetchById(userId: String, id: String): LingoSnapBO

    @Throws(FetchAllLingoSnapException::class)
    suspend fun fetchAllByUserId(userId: String): List<LingoSnapBO>
}