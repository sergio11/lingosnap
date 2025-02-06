package com.dreamsoftware.lingosnap.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IImageDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IOutfitDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.AuthUserDTO
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitDTO
import com.dreamsoftware.lingosnap.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateOutfitDTO
import com.dreamsoftware.lingosnap.data.repository.impl.IMultiModalLanguageModelRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.ImageRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.OutfitRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.PreferenceRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.UserRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.mapper.AddOutfitMessageMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.AuthUserMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.OutfitMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.ResolveQuestionMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.CreateOutfitMapper
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.model.OutfitBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.model.CreateOutfitBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.IOutfitRepository
import com.dreamsoftware.lingosnap.domain.repository.IMultiModalLanguageModelRepository
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthUserMapper(): IBrownieOneSideMapper<AuthUserDTO, AuthUserBO> = AuthUserMapper()

    @Provides
    @Singleton
    fun provideOutfitMapper(): IBrownieOneSideMapper<OutfitDTO, OutfitBO> = OutfitMapper()

    @Provides
    @Singleton
    fun provideSaveOutfitMapper(): IBrownieOneSideMapper<CreateOutfitBO, CreateOutfitDTO> = CreateOutfitMapper()

    @Provides
    @Singleton
    fun provideResolveQuestionMapper(): IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO> = ResolveQuestionMapper()

    @Provides
    @Singleton
    fun provideAddOutfitMessageMapper(): IBrownieOneSideMapper<AddMessageBO, AddMessageDTO> = AddOutfitMessageMapper()

    @Provides
    @Singleton
    fun provideUserRepository(
        authDataSource: IAuthRemoteDataSource,
        authUserMapper: IBrownieOneSideMapper<AuthUserDTO, AuthUserBO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IUserRepository =
        UserRepositoryImpl(
            authDataSource,
            authUserMapper,
            dispatcher
        )

    @Provides
    @Singleton
    fun providePreferenceRepository(
        preferenceDataSource: IPreferencesDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IPreferenceRepository =
        PreferenceRepositoryImpl(
            preferenceDataSource,
            dispatcher
        )

    @Provides
    @Singleton
    fun provideOutfitRepository(
        outfitDataSource: IOutfitDataSource,
        saveOutfitMapper: IBrownieOneSideMapper<CreateOutfitBO, CreateOutfitDTO>,
        addOutfitMapper: IBrownieOneSideMapper<AddMessageBO, AddMessageDTO>,
        outfitMapper: IBrownieOneSideMapper<OutfitDTO, OutfitBO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IOutfitRepository =
        OutfitRepositoryImpl(
            outfitDataSource,
            saveOutfitMapper,
            addOutfitMapper,
            outfitMapper,
            dispatcher
        )

    @Provides
    @Singleton
    fun provideMultiModalLanguageModelRepository(
        multiModalLanguageModelDataSource: IMultiModalLanguageModelDataSource,
        resolveQuestionMapper: IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IMultiModalLanguageModelRepository =
        IMultiModalLanguageModelRepositoryImpl(
            multiModalLanguageModelDataSource,
            resolveQuestionMapper,
            dispatcher
        )

    @Provides
    @Singleton
    fun provideImageRepository(
        imageDataSource: IImageDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IImageRepository =
        ImageRepositoryImpl(
            imageDataSource,
            dispatcher
        )
}
