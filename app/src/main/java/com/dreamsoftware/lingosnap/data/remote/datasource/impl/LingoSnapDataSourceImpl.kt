package com.dreamsoftware.lingosnap.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.datasource.ILingoSnapDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.core.SupportDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateLingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.dto.LingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.exception.AddLingoSnapMessageRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.CreateLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.DeleteLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchAllLingoSnapRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchLingoSnapByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.SearchLingoSnapRemoteDataException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await

internal class LingoSnapDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val saveLingoSnapMapper: IBrownieOneSideMapper<CreateLingoSnapDTO, Map<String, Any?>>,
    private val addLingoSnapMessageMapper: IBrownieOneSideMapper<AddMessageDTO, List<Map<String, String>>>,
    private val lingoSnapMapper: IBrownieOneSideMapper<Map<String, Any?>, LingoSnapDTO>,
    dispatcher: CoroutineDispatcher
): SupportDataSourceImpl(dispatcher), ILingoSnapDataSource {

    private companion object {
        const val COLLECTION_NAME = "lingo_snap_collection"
        const val SUB_COLLECTION_NAME = "questions"
        const val MESSAGE_FIELD_NAME = "messages"
        const val IMAGE_DESCRIPTION_FIELD_NAME = "imageDescription"
        const val CREATED_AT_FIELD_NAME = "createdAt"
    }

    private val collection by lazy {
        firestore.collection(COLLECTION_NAME)
    }

    @Throws(SearchLingoSnapRemoteDataException::class)
    override suspend fun search(userId: String, term: String): List<LingoSnapDTO> = safeExecution(
        onExecuted = {
            val snapshot = collection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .whereGreaterThanOrEqualTo(IMAGE_DESCRIPTION_FIELD_NAME, term)
                .whereLessThanOrEqualTo(IMAGE_DESCRIPTION_FIELD_NAME, term + "\uf8ff")
                .orderBy(CREATED_AT_FIELD_NAME, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { document ->
                lingoSnapMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            SearchLingoSnapRemoteDataException("Failed to search questions", ex)
        }
    )

    @Throws(CreateLingoSnapRemoteDataException::class)
    override suspend fun create(data: CreateLingoSnapDTO): Unit = safeExecution(
        onExecuted = {
            collection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
                .set(saveLingoSnapMapper.mapInToOut(data))
                .await()
        },
        onErrorOccurred = { ex ->
            CreateLingoSnapRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(AddLingoSnapMessageRemoteDataException::class)
    override suspend fun addMessage(data: AddMessageDTO): Unit = safeExecution(
        onExecuted = {
            val messages = addLingoSnapMessageMapper.mapInToOut(data)
            val documentRef = collection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
            for (message in messages) {
                documentRef.update(MESSAGE_FIELD_NAME, FieldValue.arrayUnion(message)).await()
            }
        },
        onErrorOccurred = { ex ->
            AddLingoSnapMessageRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(FetchLingoSnapByIdRemoteDataException::class)
    override suspend fun fetchById(userId: String, id: String): LingoSnapDTO =
        safeExecution(
            onExecuted = {
                val document = collection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(id)
                    .get()
                    .await()
                lingoSnapMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            },
            onErrorOccurred = { ex ->
                FetchLingoSnapByIdRemoteDataException(
                    "An error occurred when trying to fetch the user question with ID $id",
                    ex
                )
            }
        )

    @Throws(FetchAllLingoSnapRemoteDataException::class)
    override suspend fun fetchAllByUserId(userId: String): List<LingoSnapDTO> = safeExecution(
        onExecuted = {
            val snapshot = collection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .orderBy(CREATED_AT_FIELD_NAME, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { document ->
                lingoSnapMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            FetchAllLingoSnapRemoteDataException(
                "An error occurred when trying to fetch all user questions",
                ex
            )
        }
    )

    @Throws(DeleteLingoSnapByIdRemoteDataException::class)
    override suspend fun deleteById(userId: String, id: String): Unit = safeExecution(
        onExecuted = {
            collection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .document(id)
                .delete()
                .await()
        },
        onErrorOccurred = { ex ->
            DeleteLingoSnapByIdRemoteDataException(
                "An error occurred when trying to delete the user question",
                ex
            )
        }
    )
}