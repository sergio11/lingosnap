package com.dreamsoftware.lingosnap.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainImmediateDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GenerativeTextModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GenerativeMultiModalModel

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignInScreenErrorMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SignUpScreenErrorMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class HomeErrorMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatErrorMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LingoSnapDetailErrorMapper

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CreateLingoSnapErrorMapper