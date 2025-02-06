package com.dreamsoftware.lingosnap.di

import com.dreamsoftware.lingosnap.domain.model.AuthRequestBO
import com.dreamsoftware.lingosnap.domain.model.SignUpBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import com.dreamsoftware.lingosnap.domain.service.ISoundPlayerService
import com.dreamsoftware.lingosnap.domain.service.ITTSService
import com.dreamsoftware.lingosnap.domain.service.ITranscriptionService
import com.dreamsoftware.lingosnap.domain.usecase.AddOutfitMessageUseCase
import com.dreamsoftware.lingosnap.domain.usecase.CreateOutfitUseCase
import com.dreamsoftware.lingosnap.domain.usecase.DeleteOutfitByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.TranscribeUserQuestionUseCase
import com.dreamsoftware.lingosnap.domain.usecase.EndUserSpeechCaptureUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAllOutfitsByUserUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAssistantMutedStatusUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetAuthenticateUserDetailUseCase
import com.dreamsoftware.lingosnap.domain.usecase.GetOutfitByIdUseCase
import com.dreamsoftware.lingosnap.domain.usecase.SearchOutfitUseCase
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
    fun provideCreateOutfitUseCase(
        userRepository: IUserRepository,
        imageRepository: IImageRepository,
        outfitRepository: IOutfitRepository,
        multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
    ): CreateOutfitUseCase =
        CreateOutfitUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository,
            outfitRepository = outfitRepository,
            multiModalLanguageModelRepository = multiModalLanguageModelRepository
        )

    @Provides
    @ViewModelScoped
    fun provideDeleteOutfitByIdUseCase(
        userRepository: IUserRepository,
        imageRepository: IImageRepository,
        outfitRepository: IOutfitRepository
    ): DeleteOutfitByIdUseCase =
        DeleteOutfitByIdUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository,
            outfitRepository = outfitRepository
        )

    @Provides
    @ViewModelScoped
    fun provideGetAllOutfitByUserUseCase(
        userRepository: IUserRepository,
        outfitRepository: IOutfitRepository
    ): GetAllOutfitsByUserUseCase =
        GetAllOutfitsByUserUseCase(
            userRepository = userRepository,
            outfitRepository = outfitRepository
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
    fun provideGetOutfitByIdUseCase(
        userRepository: IUserRepository,
        outfitRepository: IOutfitRepository
    ): GetOutfitByIdUseCase =
        GetOutfitByIdUseCase(
            userRepository = userRepository,
            outfitRepository = outfitRepository
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
    fun provideAddOutfitQuestionUseCase(
        userRepository: IUserRepository,
        outfitRepository: IOutfitRepository,
        multiModalLanguageModelRepository: IMultiModalLanguageModelRepository
    ): AddOutfitMessageUseCase =
        AddOutfitMessageUseCase(
            userRepository = userRepository,
            outfitRepository = outfitRepository,
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
    fun provideSearchOutfitUseCase(
        userRepository: IUserRepository,
        outfitRepository: IOutfitRepository,
    ): SearchOutfitUseCase =
        SearchOutfitUseCase(
            userRepository = userRepository,
            outfitRepository = outfitRepository
        )
}
