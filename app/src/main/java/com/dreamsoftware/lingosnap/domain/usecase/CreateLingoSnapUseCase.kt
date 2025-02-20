package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.model.CreateLingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import java.util.UUID

/**
 * Use case for creating a new lingoSnap entry.
 * This involves saving an image, generating an answer using a multi-modal language model,
 * and then creating an lingoSnap record with the generated data.
 *
 * @param userRepository Repository for user-related operations.
 * @param imageRepository Repository for image-related operations.
 * @param lingoSnapRepository Repository for lingoSnap records.
 * @param multiModalLanguageModelRepository Repository for multi-modal language model interactions.
 */
class CreateLingoSnapUseCase(
    private val userRepository: IUserRepository,
    private val imageRepository: IImageRepository,
    private val lingoSnapRepository: ILingoSnapRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<CreateLingoSnapUseCase.Params, LingoSnapBO>() {

    /**
     * Executes the use case to create a new lingoSnap record.
     *
     * @param params Parameters containing the image URL and the user's question.
     * @return The newly created lingoSnap business object (lingoSnapBO).
     */
    override suspend fun onExecuted(params: Params): LingoSnapBO = with(params) {
        // Generate a unique ID for the lingoSnap entry
        val lingoSnapId = UUID.randomUUID().toString()

        // Save the image and get the new image URL
        val newImageUrl = imageRepository.save(path = imageUrl, name = lingoSnapId)

        // Generate a description for the image
        val imageDescription = multiModalLanguageModelRepository.generateImageDescription(newImageUrl)

        // Prepare the question for resolution
        val resolveQuestion = ResolveQuestionBO(
            context = imageDescription,
            question = question
        )

        // Resolve the question
        val answer = multiModalLanguageModelRepository.resolveQuestion(resolveQuestion)

        // Get the authenticated user's ID
        val userId = userRepository.getUserAuthenticatedUid()

        // Create the lingoSnap entry
        val lingoSnapBO = CreateLingoSnapBO(
            uid = lingoSnapId,
            userId = userId,
            imageUrl = newImageUrl,
            imageDescription = imageDescription,
            question = question,
            answer = answer
        )

        // Save the lingoSnap entry
        lingoSnapRepository.create(lingoSnapBO)
    }

    /**
     * Data class representing the parameters for the use case.
     *
     * @property imageUrl The URL of the image to be processed.
     * @property question The question asked by the user.
     */
    data class Params(
        val imageUrl: String,
        val question: String
    )
}