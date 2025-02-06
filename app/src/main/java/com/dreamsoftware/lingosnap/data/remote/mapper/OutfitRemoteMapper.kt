package com.dreamsoftware.lingosnap.data.remote.mapper

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitDTO
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitMessageDTO
import com.google.firebase.Timestamp

internal class OutfitRemoteMapper: IBrownieOneSideMapper<Map<String, Any?>, OutfitDTO> {

    private companion object {
        const val UID_KEY = "uid"
        const val USER_ID_KEY = "userId"
        const val IMAGE_URL_KEY = "imageUrl"
        const val IMAGE_DESCRIPTION_KEY = "imageDescription"
        const val CREATED_AT_KEY = "createdAt"
        const val MESSAGES_KEY = "messages"
        const val MESSAGE_ID_KEY = "uid"
        const val ROLE_KEY = "role"
        const val TEXT_KEY = "text"
    }

    override fun mapInToOut(input: Map<String, Any?>): OutfitDTO = with(input) {
        OutfitDTO(
            uid = get(UID_KEY) as String,
            userId = get(USER_ID_KEY) as String,
            imageUrl = get(IMAGE_URL_KEY) as String,
            imageDescription = get(IMAGE_DESCRIPTION_KEY) as String,
            createAt = get(CREATED_AT_KEY) as Timestamp,
            messages = (get(MESSAGES_KEY) as? List<Map<String, String>>)?.map {
                OutfitMessageDTO(
                    uid = it[MESSAGE_ID_KEY].orEmpty(),
                    role = it[ROLE_KEY].orEmpty(),
                    text = it[TEXT_KEY].orEmpty()
                )
            }.orEmpty()
        )
    }

    override fun mapInListToOutList(input: Iterable<Map<String, Any?>>): Iterable<OutfitDTO> =
        input.map(::mapInToOut)
}