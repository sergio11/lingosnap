package com.dreamsoftware.lingosnap.di

import com.dreamsoftware.brownie.utils.IBrownieOneSideMapper
import com.dreamsoftware.lingosnap.data.remote.datasource.IAuthRemoteDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IImageDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.IOutfitDataSource
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.AuthRemoteDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.ImageDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.datasource.impl.OutfitDataSourceImpl
import com.dreamsoftware.lingosnap.data.remote.dto.AddMessageDTO
import com.dreamsoftware.lingosnap.data.remote.dto.AuthUserDTO
import com.dreamsoftware.lingosnap.data.remote.dto.CreateOutfitDTO
import com.dreamsoftware.lingosnap.data.remote.dto.OutfitDTO
import com.dreamsoftware.lingosnap.data.remote.mapper.AddOutfitMessageRemoteMapper
import com.dreamsoftware.lingosnap.data.remote.mapper.CreateOutfitRemoteMapper
import com.dreamsoftware.lingosnap.data.remote.mapper.UserAuthenticatedMapper
import com.dreamsoftware.lingosnap.data.remote.mapper.OutfitRemoteMapper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

// Dagger module for providing Firebase-related dependencies
@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    /**
     * Provides a singleton instance of UserAuthenticatedMapper.
     * @return a new instance of UserAuthenticatedMapper.
     */
    @Provides
    @Singleton
    fun provideUserAuthenticatedMapper(): IBrownieOneSideMapper<FirebaseUser, AuthUserDTO> = UserAuthenticatedMapper()

    @Provides
    @Singleton
    fun provideUserQuestionRemoteMapper(): IBrownieOneSideMapper<Map<String, Any?>, OutfitDTO> = OutfitRemoteMapper()

    @Provides
    @Singleton
    fun provideSaveUserQuestionRemoteMapper(): IBrownieOneSideMapper<CreateOutfitDTO, Map<String, Any?>> = CreateOutfitRemoteMapper()

    @Provides
    @Singleton
    fun provideAddOutfitMessageRemoteMapper(): IBrownieOneSideMapper<AddMessageDTO, List<Map<String, String>>> = AddOutfitMessageRemoteMapper()

    /**
     * Provides a singleton instance of FirebaseAuth.
     * @return the default instance of FirebaseAuth.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Provide Firebase Store
     */
    @Provides
    @Singleton
    fun provideFirebaseStore() = Firebase.firestore

    /**
     * Provide Firebase Storage
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    /**
     * Provides a singleton instance of IAuthDataSource.
     * @param userAuthenticatedMapper the IBrownieOneSideMapper<FirebaseUser, AuthUserDTO> instance.
     * @param firebaseAuth the FirebaseAuth instance.
     * @return a new instance of AuthDataSourceImpl implementing IAuthDataSource.
     */
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(
        userAuthenticatedMapper: IBrownieOneSideMapper<FirebaseUser, AuthUserDTO>,
        firebaseAuth: FirebaseAuth,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IAuthRemoteDataSource = AuthRemoteDataSourceImpl(
        userAuthenticatedMapper,
        firebaseAuth,
        dispatcher
    )

    @Provides
    @Singleton
    fun provideUserPicturesDataSource(
        storage: FirebaseStorage,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IImageDataSource = ImageDataSourceImpl(
        storage,
        dispatcher
    )

    @Provides
    @Singleton
    fun provideOutfitDataSource(
        firestore: FirebaseFirestore,
        saveUserQuestionMapper: IBrownieOneSideMapper<CreateOutfitDTO, Map<String, Any?>>,
        addOutfitMessageMapper: IBrownieOneSideMapper<AddMessageDTO, List<Map<String, String>>>,
        userQuestionMapper: IBrownieOneSideMapper<Map<String, Any?>, OutfitDTO>,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IOutfitDataSource = OutfitDataSourceImpl(
        firestore,
        saveUserQuestionMapper,
        addOutfitMessageMapper,
        userQuestionMapper,
        dispatcher
    )
}