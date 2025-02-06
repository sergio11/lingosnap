package com.dreamsoftware.lingosnap.data.remote.datasource.impl

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.datasource.IOutfitDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.core.SupportDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateOutfitDTO
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitDTO
import com.dreamsoftware.lingosnap.data.remote.exception.AddOutfitMessageRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.CreateOutfitRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.DeleteOutfitByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchAllOutfitRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.FetchOutfitByIdRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.SearchOutfitRemoteDataException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await

internal class OutfitDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val saveOutfitMapper: IBrownieOneSideMapper<CreateOutfitDTO, Map<String, Any?>>,
    private val addOutfitMessageMapper: IBrownieOneSideMapper<AddMessageDTO, List<Map<String, String>>>,
    private val outfitMapper: IBrownieOneSideMapper<Map<String, Any?>, OutfitDTO>,
    dispatcher: CoroutineDispatcher
): SupportDataSourceImpl(dispatcher), IOutfitDataSource {

    private companion object {
        const val COLLECTION_NAME = "chicfit_outfits"
        const val SUB_COLLECTION_NAME = "questions"
        const val MESSAGE_FIELD_NAME = "messages"
        const val IMAGE_DESCRIPTION_FIELD_NAME = "imageDescription"
        const val CREATED_AT_FIELD_NAME = "createdAt"
    }

    private val collection by lazy {
        firestore.collection(COLLECTION_NAME)
    }

    @Throws(SearchOutfitRemoteDataException::class)
    override suspend fun search(userId: String, term: String): List<OutfitDTO> = safeExecution(
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
                outfitMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            SearchOutfitRemoteDataException("Failed to search questions", ex)
        }
    )

    @Throws(CreateOutfitRemoteDataException::class)
    override suspend fun create(data: CreateOutfitDTO): Unit = safeExecution(
        onExecuted = {
            collection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
                .set(saveOutfitMapper.mapInToOut(data))
                .await()
        },
        onErrorOccurred = { ex ->
            CreateOutfitRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(AddOutfitMessageRemoteDataException::class)
    override suspend fun addMessage(data: AddMessageDTO): Unit = safeExecution(
        onExecuted = {
            val messages = addOutfitMessageMapper.mapInToOut(data)
            val documentRef = collection
                .document(data.userId)
                .collection(SUB_COLLECTION_NAME)
                .document(data.uid)
            for (message in messages) {
                documentRef.update(MESSAGE_FIELD_NAME, FieldValue.arrayUnion(message)).await()
            }
        },
        onErrorOccurred = { ex ->
            AddOutfitMessageRemoteDataException("Failed to save user question", ex)
        }
    )

    @Throws(FetchOutfitByIdRemoteDataException::class)
    override suspend fun fetchById(userId: String, id: String): OutfitDTO =
        safeExecution(
            onExecuted = {
                val document = collection
                    .document(userId)
                    .collection(SUB_COLLECTION_NAME)
                    .document(id)
                    .get()
                    .await()
                outfitMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            },
            onErrorOccurred = { ex ->
                FetchOutfitByIdRemoteDataException(
                    "An error occurred when trying to fetch the user question with ID $id",
                    ex
                )
            }
        )

    @Throws(FetchAllOutfitRemoteDataException::class)
    override suspend fun fetchAllByUserId(userId: String): List<OutfitDTO> = safeExecution(
        onExecuted = {
            val snapshot = collection
                .document(userId)
                .collection(SUB_COLLECTION_NAME)
                .orderBy(CREATED_AT_FIELD_NAME, Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.map { document ->
                outfitMapper.mapInToOut(
                    document.data ?: throw IllegalStateException("Document data is null")
                )
            }
        },
        onErrorOccurred = { ex ->
            FetchAllOutfitRemoteDataException(
                "An error occurred when trying to fetch all user questions",
                ex
            )
        }
    )

    @Throws(DeleteOutfitByIdRemoteDataException::class)
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
            DeleteOutfitByIdRemoteDataException(
                "An error occurred when trying to delete the user question",
                ex
            )
        }
    )
}