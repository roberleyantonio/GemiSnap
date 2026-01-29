package br.com.dev360.gemisnap.core.networking.data

import br.com.dev360.gemisnap.core.networking.exception.Failure


sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Error(val failure: Failure) : ResultWrapper<Nothing>()

    fun <M> map(mapper: (originalData: T) -> M): ResultWrapper<M> = when (this) {
        is Success -> Success(mapper(value))
        is Error -> Error(failure = failure)
    }

    fun isSuccessful() = this is Success
    fun isError() = this is Error

    inline fun <X> withSuccessAndError(success: (T) -> X, error: (Failure) -> X): X = when (this) {
        is Success -> success(this.value)
        is Error -> error(this.failure)
    }

    fun toSuccess() = this as Success

    fun toError() = this as Error

    fun toFailure(): Failure? =
        if (isError()) {
        (this as Error).failure
    } else {
        null
    }
}

inline fun <T> ResultWrapper<T>.onSuccess(action: (value: T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) {
        action(value)
    }
    return this
}

inline fun <T> ResultWrapper<T>.onFailure(action: (failure: Failure) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Error) {
        action(failure)
    }
    return this
}

fun <T> ResultWrapper<T>.dataOrNull(): T? {
    return when (this) {
        is ResultWrapper.Success -> this.value
        is ResultWrapper.Error -> null
    }
}