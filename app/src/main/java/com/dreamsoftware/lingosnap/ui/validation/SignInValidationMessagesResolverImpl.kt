package com.dreamsoftware.lingosnap.ui.validation

import android.content.Context
import com.dreamsoftware.lingosnap.R
import com.dreamsoftware.lingosnap.domain.validation.ISignInValidationMessagesResolver

class SignInValidationMessagesResolverImpl(private val context: Context) :
    ISignInValidationMessagesResolver {
    override fun getInvalidEmailMessage(): String =
        context.getString(R.string.invalid_email_message)

    override fun getShortPasswordMessage(minLength: Int): String =
        context.getString(R.string.short_password_message, minLength)
}