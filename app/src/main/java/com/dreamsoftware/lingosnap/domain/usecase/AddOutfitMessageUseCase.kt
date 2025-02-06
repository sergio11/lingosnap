package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

/**
 * Use case for adding a new question to an existing outfit conversation.
 *
 * This use case is responsible for:
 * 1. Retrieving the currently authenticated user's ID.
 * 2. Fetching an existing outfit conversation based on the provided outfit ID.
 * 3. Resolving the answer to the new question using a multi-modal language model.
 * 4. Adding the new question and its resolved answer to the outfit conversation history.
 *
 * The use case interacts with the following repositories:
 * - `userRepository`: To retrieve the authenticated user's ID.
 * - `outfitRepository`: To fetch the outfit conversation by its ID and to add the new message to the conversation.
 * - `multiModalLanguageModelRepository`: To resolve the question using a multi-modal language model.
 */
class AddOutfitMessageUseCase(
    private val userRepository: IUserRepository,
    private val outfitRepository: IOutfitRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<AddOutfitMessageUseCase.Params, OutfitBO>() {

    /**
     * Executes the use case to add a new question to an existing outfit conversation.
     *
     * @param params The parameters required for this use case, including the outfit ID and the new question.
     * @return The updated outfit business object containing the conversation history and the new message.
     * @throws Exception If any error occurs during the execution of the use case.
     */
    override suspend fun onExecuted(params: Params): OutfitBO = with(params) {
        // Retrieve the authenticated user's ID
        val userId = userRepository.getUserAuthenticatedUid()

        // Fetch the existing outfit conversation by ID
        val outfit = outfitRepository.fetchById(userId = userId, id = outfitId)

        // Resolve the answer to the new question using the multi-modal language model
        val answer = multiModalLanguageModelRepository.resolveQuestion(ResolveQuestionBO(
            context = outfit.imageDescription,
            question = question,
            history = outfit.messages.map { it.role.name to it.text }
        ))

        // Create a new outfit message containing the question and the resolved answer
        val newOutfitMessage = AddMessageBO(
            uid = outfitId,
            userId = userId,
            question = question,
            answer = answer
        )

        // Add the new message to the Outfit conversation
        outfitRepository.addMessage(newOutfitMessage)
    }

    /**
     * Data class representing the parameters required to execute the AddOutfitMessageUseCase.
     *
     * @property outfitId The ID of the Outfit conversation to which the new question will be added.
     * @property question The new question to be added to the conversation.
     */
    data class Params(
        val outfitId: String,
        val question: String
    )
}