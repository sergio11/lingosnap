package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository

/**
 * Use case for adding a new question to an existing lingoSnap conversation.
 *
 * This use case is responsible for:
 * 1. Retrieving the currently authenticated user's ID.
 * 2. Fetching an existing lingoSnap conversation based on the provided lingoSnap ID.
 * 3. Resolving the answer to the new question using a multi-modal language model.
 * 4. Adding the new question and its resolved answer to the lingoSnap conversation history.
 *
 * The use case interacts with the following repositories:
 * - `userRepository`: To retrieve the authenticated user's ID.
 * - `lingoSnapRepository`: To fetch the lingoSnap conversation by its ID and to add the new message to the conversation.
 * - `multiModalLanguageModelRepository`: To resolve the question using a multi-modal language model.
 */
class AddLingoSnapMessageUseCase(
    private val userRepository: IUserRepository,
    private val lingoSnapRepository: ILingoSnapRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<AddLingoSnapMessageUseCase.Params, LingoSnapBO>() {

    /**
     * Executes the use case to add a new question to an existing lingoSnap conversation.
     *
     * @param params The parameters required for this use case, including the lingoSnap ID and the new question.
     * @return The updated lingoSnap business object containing the conversation history and the new message.
     * @throws Exception If any error occurs during the execution of the use case.
     */
    override suspend fun onExecuted(params: Params): LingoSnapBO = with(params) {
        // Retrieve the authenticated user's ID
        val userId = userRepository.getUserAuthenticatedUid()

        // Fetch the existing lingoSnap conversation by ID
        val lingoSnap = lingoSnapRepository.fetchById(userId = userId, id = lingoSnapId)

        // Resolve the answer to the new question using the multi-modal language model
        val answer = multiModalLanguageModelRepository.resolveQuestion(ResolveQuestionBO(
            context = lingoSnap.imageDescription,
            question = question,
            history = lingoSnap.messages.map { it.role.name to it.text }
        ))

        // Create a new lingoSnap message containing the question and the resolved answer
        val newMessage = AddMessageBO(
            uid = lingoSnapId,
            userId = userId,
            question = question,
            answer = answer
        )

        // Add the new message to the lingoSnap conversation
        lingoSnapRepository.addMessage(newMessage)
    }

    /**
     * Data class representing the parameters required to execute the AddLingoSnapMessageUseCase.
     *
     * @property lingoSnapId The ID of the lingoSnap conversation to which the new question will be added.
     * @property question The new question to be added to the conversation.
     */
    data class Params(
        val lingoSnapId: String,
        val question: String
    )
}