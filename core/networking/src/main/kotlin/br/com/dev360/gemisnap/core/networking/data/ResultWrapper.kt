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
}
