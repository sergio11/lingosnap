package com.dreamsoftware.lingosnap.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.local.preferences.datasource.IPreferencesDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IImageDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.ILingoSnapDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IMultiModalLanguageModelDataSource
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.AuthUserDTO
import com.dreamsoftware.lingosnap.data.remote.dto.LingoSnapDTO
import com.dreamsoftware.lingosnap.data.remote.dto.ResolveQuestionDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateLingoSnapDTO
import com.dreamsoftware.lingosnap.data.repository.impl.IMultiModalLanguageModelRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.ImageRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.LingoSnapRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.PreferenceRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.impl.UserRepositoryImpl
import com.dreamsoftware.lingosnap.data.repository.mapper.AddLingoSnapMessageMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.AuthUserMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.LingoSnapMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.ResolveQuestionMapper
import com.dreamsoftware.lingosnap.data.repository.mapper.CreateLingoSnapMapper
import com.dreamsoftware.lingosnap.domain.model.AddMessageBO
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.model.LingoSnapBO
import com.dreamsoftware.lingosnap.domain.model.ResolveQuestionBO
import com.dreamsoftware.lingosnap.domain.model.CreateLingoSnapBO
import com.dreamsoftware.lingosnap.domain.repository.IImageRepository
import com.dreamsoftware.lingosnap.domain.repository.ILingoSnapRepository
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
    fun provideLingoSnapMapper(): IBrownieOneSideMapper<LingoSnapDTO, LingoSnapBO> = LingoSnapMapper()

    @Provides
    @Singleton
    fun provideSaveLingoSnapMapper(): IBrownieOneSideMapper<CreateLingoSnapBO, CreateLingoSnapDTO> = CreateLingoSnapMapper()

    @Provides
    @Singleton
    fun provideResolveQuestionMapper(): IBrownieOneSideMapper<ResolveQuestionBO, ResolveQuestionDTO> = ResolveQuestionMapper()

    @Provides
    @Singleton
    fun provideAddLingoSnapMessageMapper(): IBrownieOneSideMapper<AddMessageBO, AddMessageDTO> = AddLingoSnapMessageMapper()

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
    fun provideLingoSnapRepository(
        lingoSnapDataSource: ILingoSnapDataSource,
        saveLingoSnapMapper: IBrownieOneSideMapper<CreateLingoSnapBO, CreateLingoSnapDTO>,
        addLingoSnapMapper: IBrownieOneSideMapper<AddMessageBO, AddMessageDTO>,
        lingoSnapMapper: IBrownieOneSideMapper<LingoSnapDTO, LingoSnapBO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): ILingoSnapRepository =
        LingoSnapRepositoryImpl(
            lingoSnapDataSource,
            saveLingoSnapMapper,
            addLingoSnapMapper,
            lingoSnapMapper,
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
