package com.dreamsoftware.lingosnap.domain.usecase

import com.dreamsoftware.brownie.core.BrownieUseCaseWithParams
import com.dreamsoftware.lingosnap.domain.model.AuthUserBO
import com.dreamsoftware.lingosnap.domain.model.SignUpBO
import com.dreamsoftware.lingosnap.domain.repository.IPreferenceRepository
import com.dreamsoftware.lingosnap.domain.repository.IUserRepository
import com.dreamsoftware.lingosnap.domain.exception.InvalidDataException
import com.dreamsoftware.lingosnap.domain.validation.IBusinessEntityValidator

class SignUpUseCase(
    private val preferenceRepository: IPreferenceRepository,
    private val userRepository: IUserRepository,
    private val validator: IBusinessEntityValidator<SignUpBO>
): BrownieUseCaseWithParams<SignUpUseCase.Params, AuthUserBO>() {

    override suspend fun onExecuted(params: Params): AuthUserBO = with(params) {
        params.toSignUpBO().let { signUpBO ->
            validator.validate(signUpBO).takeIf { it.isNotEmpty() }?.let { errors ->
                throw InvalidDataException(errors, "Invalid data provided")
            } ?: run {
                userRepository.signUp(signUpBO).also {
                    preferenceRepository.saveAuthUserUid(it.uid)
                }
            }
        }
    }

    private fun Params.toSignUpBO() =
        SignUpBO(
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )

    data class Params(
        val email: String,
        val password: String,
        val confirmPassword: String
    )
}