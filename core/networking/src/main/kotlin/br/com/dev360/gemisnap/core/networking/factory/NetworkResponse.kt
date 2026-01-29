package br.com.dev360.gemisnap.core.networking.factory

import br.com.dev360.gemisnap.core.networking.data.ResultWrapper
import br.com.dev360.gemisnap.core.networking.exception.Failure

sealed class NetworkResponse<out T> {
    data class Success<out T>(
        val value: T
    ) : NetworkResponse<T>()

    data class Error(
        @Transient
        val exception: Failure? = null
    ) : NetworkResponse<Nothing>()
}

fun <T> NetworkResponse<T>.toResult(): ResultWrapper<T> =
    when (this) {
        is NetworkResponse.Success -> {
            ResultWrapper.Success(this.value)
        }

        is NetworkResponse.Error -> {
            ResultWrapper.Error(failure = this.exception ?: Failure.GenericError())
        }
    }