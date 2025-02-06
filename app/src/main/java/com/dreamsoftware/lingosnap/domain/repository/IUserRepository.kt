package com.dreamsoftware.lingosnap.domain.repository

import com.dreamsoftware.lingosnap.domain.exception.CheckAuthenticatedException
import com.dreamsoftware.lingosnap.domain.exception.CloseSessionException
import com.dreamsoftware.lingosnap.domain.exception.SignInException
import com.dreamsoftware.lingosnap.domain.exception.SignUpException
import com.dreamsoftware.lingosnap.domain.model.AuthRequestBO
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.model.SignUpBO

interface IUserRepository {

    @Throws(CheckAuthenticatedException::class)
    suspend fun getCurrentAuthenticatedUser(): AuthUserBO

    @Throws(CheckAuthenticatedException::class)
    suspend fun getUserAuthenticatedUid(): String

    @Throws(SignInException::class)
    suspend fun signIn(authRequest: AuthRequestBO): AuthUserBO

    @Throws(SignUpException::class)
    suspend fun signUp(data: SignUpBO): AuthUserBO

    @Throws(CloseSessionException::class)
    suspend fun closeSession()
}