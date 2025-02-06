package com.dreamsoftware.lingosnap.data.remote.exception

open class RemoteDataException(message: String? = null, cause: Throwable? = null): Exception(message, cause)

// Auth Data Source
class AuthRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignInRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class SignUpRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Images Data Source
class SavePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeletePictureRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Outfit Data Source
class SearchOutfitRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class CreateOutfitRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class AddOutfitMessageRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchOutfitByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class FetchAllOutfitRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class DeleteOutfitByIdRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)

// Multimodal LLM Data Source
class ResolveQuestionFromContextRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)
class GenerateImageDescriptionRemoteDataException(message: String? = null, cause: Throwable? = null): RemoteDataException(message, cause)