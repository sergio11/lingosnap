package com.dreamsoftware.lingosnap.domain.validation.impl

import android.util.Patterns
import com.dreamsoftware.lingosnap.domain.model.AuthRequestBO
import com.dreamsoftware.lingosnap.domain.validation.IBusinessEntityValidator
import com.dreamsoftware.lingosnap.domain.validation.ISignInValidationMessagesResolver

internal class SignInValidatorImpl(
    private val messagesResolver: ISignInValidationMessagesResolver
): IBusinessEntityValidator<AuthRequestBO> {

    private companion object {
        const val MIN_PASSWORD_LENGTH = 6
    }

    override fun validate(entity: AuthRequestBO): Map<String, String> = buildMap {
        with(entity) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                put(AuthRequestBO.FIELD_EMAIL, messagesResolver.getInvalidEmailMessage())
            }
            if (password.length < MIN_PASSWORD_LENGTH) {
                put(AuthRequestBO.FIELD_PASSWORD, messagesResolver.getShortPasswordMessage(MIN_PASSWORD_LENGTH))
            }
        }
    }
}