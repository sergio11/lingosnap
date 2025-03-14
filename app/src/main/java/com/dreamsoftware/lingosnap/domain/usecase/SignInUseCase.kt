package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.exception.InvalidDataException
import com.dreamsoftware.lingosnap.domain.model.AuthRequestBO
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import com.dreamsoftware.lingosnap.domain.validation.IBusinessEntityValidator

/**
 * SignIn Use Case
 * @param userRepository
 * @param preferenceRepository
 * @param validator
 */
class SignInUseCase(
    private val userRepository: IUserRepository,
    private val preferenceRepository: IPreferenceRepository,
    private val validator: IBusinessEntityValidator<AuthRequestBO>
) : BrownieUseCaseWithParams<SignInUseCase.Params, AuthUserBO>() {

    override suspend fun onExecuted(params: Params): AuthUserBO = with(params) {
        params.toAuthRequestBO().let { authUserBO ->
            validator.validate(authUserBO).takeIf { it.isNotEmpty() }?.let { errors ->
                throw InvalidDataException(errors, "Invalid data provided")
            } ?: run {
                userRepository.signIn(authUserBO).also {
                    preferenceRepository.saveAuthUserUid(uid = it.uid)
                }
            }
        }
    }

    private fun Params.toAuthRequestBO() =
        AuthRequestBO(
            email = email,
            password = password
        )

    data class Params(
        val email: String,
        val password: String
    )
}