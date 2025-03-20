package com.cox.domain.util

sealed class ErrorType {
    data object NetworkError : ErrorType()
    data object ServerError : ErrorType()
    data object UnknownError : ErrorType()
    data object DatabaseError : ErrorType()
    data object BusinessError : ErrorType()
}

class DomainErrorFactory(
    val errorCode: Int,
    val errorType: ErrorType,
    val userFriendlyMessage: String,
    val developerMessage: String? = null
) {

    companion object {
        /**
         * Creates a DomainErrorFactory from an HTTP status code.
         * @param httpCode The HTTP status code.
         * @return A DomainErrorFactory instance.
         */
        fun fromHttpCode(httpCode: Int): DomainErrorFactory {
            return when (httpCode) {
                in 400..499 -> DomainErrorFactory(
                    errorCode = httpCode,
                    errorType = ErrorType.ServerError,
                    userFriendlyMessage = "Request error. Please check your input.",
                    developerMessage = "HTTP Error: $httpCode"
                )

                in 500..599 -> DomainErrorFactory(
                    errorCode = httpCode,
                    errorType = ErrorType.ServerError,
                    userFriendlyMessage = "Server error. Please try again later.",
                    developerMessage = "Server Error: $httpCode"
                )

                else -> DomainErrorFactory(
                    errorCode = httpCode,
                    errorType = ErrorType.UnknownError,
                    userFriendlyMessage = "Something went wrong. Please try again.",
                    developerMessage = "Unknown Error: $httpCode"
                )
            }
        }

        /**
         * Creates a DomainErrorFactory from an exception.
         * @param exception The exception that occurred.
         * @return A DomainErrorFactory instance.
         */
        fun fromException(exception: Exception): DomainErrorFactory {
            return DomainErrorFactory(
                errorCode = exception.hashCode(),
                errorType = ErrorType.NetworkError,
                userFriendlyMessage = "Connection error. Please check your internet connection.",
                developerMessage = "Exception: ${exception.message}"
            )
        }

        /**
         * Creates a DomainErrorFactory for business logic errors.
         * @param message A custom error message.
         * @return A DomainErrorFactory instance.
         */
        fun businessError(message: String): DomainErrorFactory {
            return DomainErrorFactory(
                errorCode = -1, // Custom code for business errors
                errorType = ErrorType.BusinessError,
                userFriendlyMessage = message,
                developerMessage = "Business Error: $message"
            )
        }
    }

    override fun toString(): String {
        return "DomainErrorFactory(errorCode=$errorCode, errorType=$errorType, userFriendlyMessage='$userFriendlyMessage', developerMessage=$developerMessage)"
    }
}
