package br.com.dev360.gemisnap.core.networking.factory

import br.com.dev360.gemisnap.core.networking.data.ResultWrapper
import br.com.dev360.gemisnap.core.networking.exception.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

fun <T> NetworkResponse<T>.toFlow(): Flow<T> {
    val networkResponse = this

    return flow {
        when (networkResponse) {
            is NetworkResponse.Success -> {
                this.emit(networkResponse.value)
            }

            is NetworkResponse.Error -> {
                throw networkResponse.exception ?: Failure.GenericError()
            }
        }
    }
}

fun <T, R> Flow<NetworkResponse<T?>>.handleResult(transform: (T?) -> R): Flow<ResultWrapper<R>> {
    return this.map { result ->
        when (result) {
            is NetworkResponse.Success -> ResultWrapper.Success(transform(result.value))
            is NetworkResponse.Error -> result.toResult()
        }
    }
}

fun <T, R> Flow<NetworkResponse<List<T>?>>.handleListResult(transform: (T) -> R): Flow<ResultWrapper<List<R>>> {
    return this.map { result ->
        when (result) {
            is NetworkResponse.Success -> {
                val transformedList = result.value?.map { transform(it) }.orEmpty()
                ResultWrapper.Success(transformedList)
            }

            is NetworkResponse.Error -> result.toResult()
        }
    }
}

fun <T, R> NetworkResponse<List<T>?>.transformList(transform: (T) -> R): NetworkResponse<List<R>> {
    return when (this) {
        is NetworkResponse.Success -> {
            val transformedList = value?.map { transform(it) }.orEmpty()
            NetworkResponse.Success(transformedList)
        }

        is NetworkResponse.Error -> this
    }
}


fun <T, R> NetworkResponse<T?>.handleResult(transform: (T?) -> R): ResultWrapper<R> {
    return when (this) {
        is NetworkResponse.Success -> ResultWrapper.Success(transform(this.value))
        is NetworkResponse.Error -> this.toResult()
    }
}

fun <T, R> NetworkResponse<List<T>?>.handleListResult(transform: (T) -> R): ResultWrapper<List<R>> {
    return when (this) {
        is NetworkResponse.Success -> {
            val transformedList = this.value?.map { transform(it) }.orEmpty()
            ResultWrapper.Success(transformedList)
        }

        is NetworkResponse.Error -> this.toResult()
    }
}