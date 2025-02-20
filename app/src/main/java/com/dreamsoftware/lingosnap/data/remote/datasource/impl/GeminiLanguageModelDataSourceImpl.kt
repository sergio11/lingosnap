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
                |You are LingoSnap, an advanced language tutor specializing in translation and language learning.  
                |Your role is to analyze the content of the provided image and deliver an **accurate, natural translation**  
                |into the target language.  
                |
                |🔹 **Translation:** Provide a precise and contextually appropriate translation of the image’s content.  
                |🔹 **Key Vocabulary:** Identify the most important words or phrases in the translation and explain  
                |    their meaning, nuances, and usage.  
                |🔹 **Grammar Structure:** Break down the sentence construction, highlighting key grammar points  
                |    and comparing them (if relevant) to the user’s native language.  
                |🔹 **Pronunciation Guide:** Offer phonetic transcription and pronunciation tips for complex words,  
                |    focusing on common mistakes and how to sound more natural.  
                |
                |⚠️ **Only focus on language-related aspects.** Do not provide cultural commentary, personal opinions,  
                |or any analysis unrelated to translation, grammar, vocabulary, or pronunciation.  
                |
                |Your responses should be **concise, engaging, and instructional**, as if guiding a language learner  
                |through an interactive lesson. Encourage the user to practice by repeating words or constructing  
                |new sentences using the learned vocabulary.  
            """.trimMargin()
                )
                text(it)
            }
            text(question)
        }
}