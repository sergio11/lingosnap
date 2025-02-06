package com.dreamsoftware.lingosnap.data.remote.datasource.impl

import com.dreamsoftware.lingosnap.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.core.SupportDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.lingosnap.data.remote.exception.GenerateImageDescriptionRemoteDataException
import com.dreamsoftware.lingosnap.data.remote.exception.ResolveQuestionFromContextRemoteDataException
import com.dreamsoftware.lingosnap.utils.urlToBitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineDispatcher

internal class GeminiLanguageModelDataSourceImpl(
    private val generativeTextModel: GenerativeModel,
    private val generativeMultiModalModel: GenerativeModel,
    private val dispatcher: CoroutineDispatcher
) : SupportDataSourceImpl(dispatcher), IMultiModalLanguageModelDataSource {

    private companion object {
        const val USER = "user"
    }

    @Throws(ResolveQuestionFromContextRemoteDataException::class)
    override suspend fun resolveQuestionFromContext(data: ResolveQuestionDTO): String =
        safeExecution(
            onExecuted = {
                val currentChatSession = generativeTextModel.startChat(data.history.map {
                    content(it.role) { text(it.text) }
                })
                // Generate prompt combining the context and the user's question
                val prompt = generatePrompt(data.question, data.context)
                // Send the message and return the response
                currentChatSession.sendMessage(prompt).text
                    ?: throw IllegalStateException("Answer couldn't be obtained")
            },
            onErrorOccurred = { ex ->
                ResolveQuestionFromContextRemoteDataException(
                    "An error occurred when trying to resolver user question",
                    ex
                )
            }
        )

    /**
     * Generates a detailed description of the image located at the given [imageUrl].
     *
     * @param imageUrl The URL of the image for which a description needs to be generated.
     * @return A detailed description of the image, or `null` if an error occurs.
     */
    @Throws(GenerateImageDescriptionRemoteDataException::class)
    override suspend fun generateImageDescription(imageUrl: String): String = safeExecution(
        onExecuted = {
            val bitmap = imageUrl.urlToBitmap(dispatcher)
                ?: throw IllegalStateException("bitmap couldn't be obtained")
            generativeMultiModalModel.generateContent(content(USER) {
                image(bitmap)
                text("Provide a detailed description of the contents of the image.")
            }).text ?: throw IllegalStateException("Description couldn't be obtained")
        },
        onErrorOccurred = { ex ->
            GenerateImageDescriptionRemoteDataException(
                "An error occurred when trying to generate the image description",
                ex
            )
        }
    )

    private fun generatePrompt(question: String, imageDescription: String?): Content =
        content(USER) {
            imageDescription?.let {
                text(
                    """
                |You are ChicFit, an expert fashion stylist and personal wardrobe consultant. Your role is to provide 
                |detailed, professional, and stylish outfit recommendations based on the user's outfit photo.  
                |
                |Analyze the provided outfit and offer precise suggestions on how to improve, style, or accessorize it.  
                |Describe what type of events or situations the outfit is best suited for, considering factors like formality,  
                |seasonality, and fashion trends.  
                |
                |Be direct, insightful, and engaging in your advice. Avoid vague or overly generic responses—focus on  
                |specific details about colors, textures, fit, and how the outfit complements the wearer.  
                |
                |If the user has asked a specific question about the outfit, ensure your response directly addresses  
                |their concern while offering additional valuable fashion insights.  
                |
                |Keep a friendly and professional tone, as if you were a high-end stylist giving a personal consultation.  
                |Do not mention that your insights are based on a description—respond as if you are directly  
                |analyzing the outfit in front of you.
            """.trimMargin()
                )
                text(it)
            }
            text(question)
        }
}