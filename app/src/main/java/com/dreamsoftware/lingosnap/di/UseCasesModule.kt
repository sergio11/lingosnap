package com.dreamsoftware.lingosnap.di

import com.dreamsoftware.lingosnap.domain.model.AuthRequestBO
import com.dreamsoftware.lingosnap.domain.model.SignUpBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import com.dreamsoftware.lingosnap.domain.service.ISoundPlayerService
import com.dreamsoftware.lingosnap.domain.service.ITTSService
import com.dreamsoftware.lingosnap.domain.service.ITranscriptionService
import com.dreamsoftware.lingosnap.domain.usecase.AddLingoSnapMessageUseCase
import com.dreamsoftware.lingosnap.domain.usecase.CreateLingoSnapUseCase
import com.dreamsoftware.lingosnap.domain.usecase.DeleteLingoSnapByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.lingosnap.domain.usecase.EndUserSpeechCaptureUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAllLingoSnapsByUserUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAssistantMutedStatusUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAuthenticateUserDetailUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetLingoSnapByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SearchLingoSnapUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SignInUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SignOffUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SignUpUseCase
import com.dreamsoftware.lingosnap.domain.usecase.StopTextToSpeechUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TextToSpeechUseCase
import com.dreamsoftware.lingosnap.domain.usecase.UpdateAssistantMutedStatusUseCase
import com.dreamsoftware.lingosnap.domain.usecase.VerifyUserSessionUseCase
import com.dreamsoftware.lingosnap.domain.validation.IBusinessEntityValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideVerifyUserSessionUseCase(
        userRepository: IUserRepository
    ): VerifyUserSessionUseCase =
        VerifyUserSessionUseCase(userRepository)

    @Provides
    @ViewModelScoped
    fun provideTranscribeUserQuestionUseCase(
        transcriptionService: ITranscriptionService
    ): TranscribeUserQuestionUseCase =
        TranscribeUserQuestionUseCase(transcriptionService)

    @Provides
    @ViewModelScoped
    fun provideEndUserSpeechCaptureUseCase(
        transcriptionService: ITranscriptionService
    ): EndUserSpeechCaptureUseCase =
        EndUserSpeechCaptureUseCase(transcriptionService)

    @Provides
    @ViewModelScoped
    fun provideSignInUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository,
        validator: IBusinessEntityValidator<AuthRequestBO>
    ): SignInUseCase =
        SignInUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository,
            validator = validator
        )

    @Provides
    @ViewModelScoped
    fun provideSignUpUseCase(
        preferenceRepository: IPreferenceRepository,
        userRepository: IUserRepository,
        validator: IBusinessEntityValidator<SignUpBO>
    ): SignUpUseCase =
        SignUpUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository,
            validator = validator
        )

    @Provides
    @ViewModelScoped
    fun provideSignOffUseCase(
        userRepository: IUserRepository,
        preferenceRepository: IPreferenceRepository,
    ): SignOffUseCase =
        SignOffUseCase(
            userRepository = userRepository,
            preferenceRepository = preferenceRepository
        )

    @Provides
    @ViewModelScoped
    fun provideCreateLingoSnapUseCase(
        userRepository: IUserRepository,
        imageRepository: IImageRepository,
        lingoSnapRepository: ILingoSnapRepository,
        multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
    ): CreateLingoSnapUseCase =
        CreateLingoSnapUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository,
            lingoSnapRepository = lingoSnapRepository,
            multiModalLanguageModelRepository = multiModalLanguageModelRepository
        )

    @Provides
    @ViewModelScoped
    fun provideDeleteLingoSnapByIdUseCase(
        userRepository: IUserRepository,
        imageRepository: IImageRepository,
        lingoSnapRepository: ILingoSnapRepository
    ): DeleteLingoSnapByIdUseCase =
        DeleteLingoSnapByIdUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository,
            lingoSnapRepository = lingoSnapRepository
        )

    @Provides
    @ViewModelScoped
    fun provideGetAllLingoSnapByUserUseCase(
        userRepository: IUserRepository,
        lingoSnapRepository: ILingoSnapRepository
    ): GetAllLingoSnapsByUserUseCase =
        GetAllLingoSnapsByUserUseCase(
            userRepository = userRepository,
            lingoSnapRepository = lingoSnapRepository
        )

    @Provides
    @ViewModelScoped
    fun provideGetAuthenticateUserDetailUseCase(
        userRepository: IUserRepository
    ): GetAuthenticateUserDetailUseCase =
        GetAuthenticateUserDetailUseCase(
            userRepository = userRepository,
        )

    @Provides
    @ViewModelScoped
    fun provideGetLingoSnapByIdUseCase(
        userRepository: IUserRepository,
        lingoSnapRepository: ILingoSnapRepository
    ): GetLingoSnapByIdUseCase =
        GetLingoSnapByIdUseCase(
            userRepository = userRepository,
            lingoSnapRepository = lingoSnapRepository
        )

    @Provides
    @ViewModelScoped
    fun provideTextToSpeechUseCase(
        ttsService: ITTSService
    ): TextToSpeechUseCase =
        TextToSpeechUseCase(
            ttsService = ttsService
        )

    @Provides
    @ViewModelScoped
    fun provideAddLingoSnapQuestionUseCase(
        userRepository: IUserRepository,
        lingoSnapRepository: ILingoSnapRepository,
        multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
    ): AddLingoSnapMessageUseCase =
        AddLingoSnapMessageUseCase(
            userRepository = userRepository,
            lingoSnapRepository = lingoSnapRepository,
            multiModalLanguageModelRepository = multiModalLanguageModelRepository
        )


    @Provides
    @ViewModelScoped
    fun provideStopTextToSpeechUseCase(
        ttsService: ITTSService
    ): StopTextToSpeechUseCase =
        StopTextToSpeechUseCase(
            ttsService = ttsService
        )


    @Provides
    @ViewModelScoped
    fun provideUpdateAssistantMutedStatusUseCase(
        preferencesRepository: IPreferenceRepository,
        soundPlayerService: ISoundPlayerService
    ): UpdateAssistantMutedStatusUseCase =
        UpdateAssistantMutedStatusUseCase(
            preferencesRepository = preferencesRepository,
            soundPlayerService = soundPlayerService
        )

    @Provides
    @ViewModelScoped
    fun provideGetAssistantMutedStatusUseCase(
        preferencesRepository: IPreferenceRepository
    ): GetAssistantMutedStatusUseCase =
        GetAssistantMutedStatusUseCase(
            preferencesRepository = preferencesRepository
        )

    @Provides
    @ViewModelScoped
    fun provideSearchLingoSnapUseCase(
        userRepository: IUserRepository,
        lingoSnapRepository: ILingoSnapRepository,
    ): SearchLingoSnapUseCase =
        SearchLingoSnapUseCase(
            userRepository = userRepository,
            lingoSnapRepository = lingoSnapRepository
        )
}
