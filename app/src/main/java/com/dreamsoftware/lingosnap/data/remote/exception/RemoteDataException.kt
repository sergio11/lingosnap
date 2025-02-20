package com.dreamsoftware.lingosnap.data.remote.exception

open class RemoteDataException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignInRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignUpRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Images Data Source
class SavePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeletePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// LingoSnap Data Source
class SearchLingoSnapRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class CreateLingoSnapRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class AddLingoSnapMessageRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchLingoSnapByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchAllLingoSnapRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteLingoSnapByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Multimodal LLM Data Source
class ResolveQuestionFromContextRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class GenerateImageDescriptionRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)