package br.com.dev360.gemisnap.core.networking.exception

import br.com.dev360.gemisnap.core.shared.extensions.empty


sealed class Failure(
    open val codeStatusBackEnd: String? = String.empty(),
    val errorMessage: String = String.empty(),
    open val codeStatusResponse: String? = String.empty(),
    open val url: String? = String.empty()
) : java.lang.Exception() {

    data class NoDataAvailable(override val codeStatusBackEnd: String? = null) :
        Failure(codeStatusBackEnd, "No data available")

    data class NoDataContent(
        override val codeStatusBackEnd: String? = null,
        val errorMessageCustom: String? = null
    ) : Failure(codeStatusBackEnd, errorMessageCustom ?: "No data content")

    data class RequestError(
        override val codeStatusResponse: String? = null,
        private val msg: String? = null,
        override val url: String? = String.empty()
    ) : Failure(
        errorMessage = msg ?: "Error loading data!",
        codeStatusResponse = codeStatusResponse,
        url = url
    )

    data class GenericError(
        override val codeStatusBackEnd: String? = String.empty(),
        private val msg: String? = null
    ) : Failure(
        codeStatusBackEnd, msg ?: DEFAULT_MSG
    )

    data class LoadingState(
        override val codeStatusBackEnd: String? = String.empty(),
        private val msg: String? = null
    ) : Failure(
        codeStatusBackEnd, msg ?: DEFAULT_MSG
    )

    data class UnknownError(
        override val codeStatusBackEnd: String? = null,
        private val throwable: Throwable? = Exception(),
        override val url: String? = String.empty()
    ) : Failure(
        codeStatusBackEnd, throwable?.message ?: DEFAULT_MSG
    )

    data class LocalError(
        override val codeStatusBackEnd: String? = null,
        private val throwable: Throwable? = Exception(),
        override val url: String? = String.empty()
    ) : Failure(
        codeStatusBackEnd, throwable?.message ?: DEFAULT_MSG
    )

    data class NetworkError(
        override val codeStatusBackEnd: String? = "-1200",
        private val throwable: Throwable
    ) : Failure(
        codeStatusBackEnd,
        "Sem conexão."
    )

    open class ServerError(
        override val codeStatusBackEnd: String? = null,
        val customMessage: String? = String.empty(),
        override val codeStatusResponse: String? = String.empty(),
        override val url: String? = String.empty()
    ) : Failure(
        codeStatusBackEnd = codeStatusBackEnd,
        errorMessage = customMessage ?: "Server error",
        codeStatusResponse = codeStatusResponse,
        url = url
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ServerError

            if (codeStatusBackEnd != other.codeStatusBackEnd) return false
            if (customMessage != other.customMessage) return false
            if (codeStatusResponse != other.codeStatusResponse) return false
            if (url != other.url) return false

            return true
        }

        override fun hashCode(): Int {
            var result = codeStatusBackEnd?.hashCode() ?: 0
            result = 31 * result + (customMessage?.hashCode() ?: 0)
            result = 31 * result + (codeStatusResponse?.hashCode() ?: 0)
            result = 31 * result + (url?.hashCode() ?: 0)

            return result
        }
    }


    companion object {
        private const val DEFAULT_MSG = "Não foi possível concluir."
    }
}