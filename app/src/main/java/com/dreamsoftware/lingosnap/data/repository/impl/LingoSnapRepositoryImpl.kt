package com.dreamsoftware.lingosnap.data.repository.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.datasource.ILingoSnapDataSource
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateLingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.dto.LingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.exception.AddLingoSnapMessageRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.CreateLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.DeleteLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchAllLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.SearchLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.repository.impl.core.SupportRepositoryImpl
import com.dreamsoftware.lingosnap.domain.exception.AddLingoSnapMessageException
import com.dreamsoftware.lingosnap.domain.exception.DeleteLingoSnapByIdException
import com.dreamsoftware.lingosnap.domain.exception.FetchAllLingoSnapException
import com.dreamsoftware.lingosnap.domain.exception.FetchLingoSnapByIdException
import com.dreamsoftware.lingosnap.domain.exception.SaveLingoSnapException
import com.dreamsoftware.lingosnap.domain.exception.SearchLingoSnapException
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.CreateLingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class LingoSnapRepositoryImpl(
    private val outfitDataSource: ILingoSnapDataSource,
    private val saveOutfitMapper: IBrownieOneSideMapper<CreateLingoSnapBO, CreateLingoSnapDTO>,
    private val addOutfitMapper: IBrownieOneSideMapper<AddMessageBO, AddMessageDTO>,
    private val outfitMapper: IBrownieOneSideMapper<LingoSnapDTO, LingoSnapBO>,
    dispatcher: CoroutineDispatcher
): SupportRepositoryImpl(dispatcher), ILingoSnapRepository {

    @Throws(SearchLingoSnapException::class)
    override suspend fun search(userId: String, term: String): List<LingoSnapBO> = safeExecute {
        try {
            outfitDataSource.search(userId, term)
                .let(outfitMapper::mapInListToOutList)
                .toList()
        } catch (ex: SearchLingoSnapRemoteDataException) {
            ex.printStackTrace()
            throw SearchLingoSnapException("An error occurred when searching content", ex)
        }
    }

    @Throws(SaveLingoSnapException::class)
    override suspend fun create(data: CreateLingoSnapBO): LingoSnapBO = safeExecute {
        try {
            with(outfitDataSource) {
                create(saveOutfitMapper.mapInToOut(data))
                outfitMapper.mapInToOut(fetchById(userId = data.userId, id = data.uid))
            }
        } catch (ex: CreateLingoSnapRemoteDataException) {
            ex.printStackTrace()
            throw SaveLingoSnapException("An error occurred when trying to save outfit", ex)
        }
    }

    @Throws(AddLingoSnapMessageException::class)
    override suspend fun addMessage(data: AddMessageBO): LingoSnapBO = safeExecute {
        try {
            with(outfitDataSource) {
                addMessage(addOutfitMapper.mapInToOut(data))
                outfitMapper.mapInToOut(fetchById(userId = data.userId, id = data.uid))
            }
        } catch (ex: AddLingoSnapMessageRemoteDataException) {
            ex.printStackTrace()
            throw AddLingoSnapMessageException("An error occurred when trying to save new message", ex)
        }
    }

    @Throws(DeleteLingoSnapByIdException::class)
    override suspend fun deleteById(userId: String, id: String) {
        safeExecute {
            try {
                outfitDataSource.deleteById(userId = userId, id = id)
            } catch (ex: DeleteLingoSnapByIdRemoteDataException) {
                ex.printStackTrace()
                throw DeleteLingoSnapByIdException("An error occurred when trying to delete the outfit", ex)
            }
        }
    }

    @Throws(FetchLingoSnapByIdException::class)
    override suspend fun fetchById(userId: String, id: String): LingoSnapBO = safeExecute {
        try {
            outfitDataSource.fetchById(userId = userId, id = id)
                .let(outfitMapper::mapInToOut)
        } catch (ex: FetchLingoSnapByIdRemoteDataException) {
            ex.printStackTrace()
            throw FetchLingoSnapByIdException("An error occurred when trying to fetch the outfit data", ex)
        }
    }

    @Throws(FetchAllLingoSnapException::class)
    override suspend fun fetchAllByUserId(userId: String): List<LingoSnapBO> = safeExecute {
        try {
            outfitDataSource.fetchAllByUserId(userId = userId)
                .let(outfitMapper::mapInListToOutList)
                .toList()
        } catch (ex: FetchAllLingoSnapRemoteDataException) {
            ex.printStackTrace()
            throw FetchAllLingoSnapException("An error occurred when trying to fetch all user questions", ex)
        }
    }
}