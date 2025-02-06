package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.model.CreateOutfitBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import java.util.UUID

/**
 * Use case for creating a new outfit entry.
 * This involves saving an image, generating an answer using a multi-modal language model,
 * and then creating an outfit record with the generated data.
 *
 * @param userRepository Repository for user-related operations.
 * @param imageRepository Repository for image-related operations.
 * @param outfitRepository Repository for outfit records.
 * @param multiModalLanguageModelRepository Repository for multi-modal language model interactions.
 */
class CreateOutfitUseCase(
    private val userRepository: IUserRepository,
    private val imageRepository: IImageRepository,
    private val outfitRepository: IOutfitRepository,
    private val multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
) : BrownieUseCaseWithParams<CreateOutfitUseCase.Params, OutfitBO>() {

    /**
     * Executes the use case to create a new outfit record.
     *
     * @param params Parameters containing the image URL and the user's question.
     * @return The newly created outfit business object (OutfitBO).
     */
    override suspend fun onExecuted(params: Params): OutfitBO = with(params) {
        // Generate a unique ID for the outfit entry
        val outfitId = UUID.randomUUID().toString()

        // Save the image and get the new image URL
        val newImageUrl = imageRepository.save(path = imageUrl, name = outfitId)

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

        // Create the Outfit entry
        val outfitBO = CreateOutfitBO(
            uid = outfitId,
            userId = userId,
            imageUrl = newImageUrl,
            imageDescription = imageDescription,
            question = question,
            answer = answer
        )

        // Save the Outfit entry
        outfitRepository.create(outfitBO)
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