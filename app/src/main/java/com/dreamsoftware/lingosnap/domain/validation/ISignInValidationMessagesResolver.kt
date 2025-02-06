package com.dreamsoftware.lingosnap.domain.validation

interface ISignInValidationMessagesResolver {
    fun getInvalidEmailMessage(): String
    fun getShortPasswordMessage(minLength: Int): String
}