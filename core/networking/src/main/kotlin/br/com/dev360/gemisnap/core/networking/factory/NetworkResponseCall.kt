package br.com.dev360.gemisnap.core.networking.factory

import br.com.dev360.gemisnap.core.networking.data.FailureResponse
import br.com.dev360.gemisnap.core.networking.exception.Failure
import com.google.gson.Gson
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

@Suppress("UNCHECKED_CAST")
class NetworkResponseCall<T>(
    proxy: Call<T>
) : CallDelegate<T, NetworkResponse<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResponse<T>>) =
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()
                val url = response.raw().request.url

                val result = if (response.isSuccessful) {
                    if (body != null) {
                        NetworkResponse.Success(body)
                    } else {
                        responseEmpty()
                    }
                } else {
                    responseError(error, code, url.toString())
                }

                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(result)
                )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResponse = when (t) {
                    is IOException -> Failure.NetworkError(throwable = t)
                    else -> Failure.UnknownError(throwable = t)
                }

                callback.onResponse(
                    this@NetworkResponseCall,
                    Response.success(NetworkResponse.Error(networkResponse))
                )
            }
        })

    fun responseEmpty() = try {
        NetworkResponse.Success(Unit as T)
    } catch (ex: Exception) {
        NetworkResponse.Error(exception = Failure.NoDataContent())
    }

    private fun responseError(
        error: ResponseBody?,
        httpCode: Int,
        url: String
    ): NetworkResponse<T> {
        val errorContent = error?.string()
        val failureResponse = when {
            error == null -> null
            error.contentLength() == 0L -> null
            else -> try {
                Gson().fromJson(errorContent, FailureResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }

         return if (failureResponse != null) {
            NetworkResponse.Error(
                exception = Failure.ServerError(
                    codeStatusBackEnd = failureResponse.getCodeResponse(),
                    customMessage = failureResponse.getMessageResponse(),
                    codeStatusResponse = httpCode.toString(),
                    url = url
                )
            )
        } else if (!errorContent.isNullOrEmpty()) {
            NetworkResponse.Error(
                exception = Failure.RequestError(
                    httpCode.toString(),
                    errorContent,
                    url = url
                )
            )
        } else {
            NetworkResponse.Error(
                exception = Failure.UnknownError(
                    httpCode.toString(),
                    url = url
                )
            )
        }
    }

    override fun cloneImpl(): Call<NetworkResponse<T>> =
        NetworkResponseCall(proxy.clone())
}

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
    override fun timeout(): Timeout = proxy.timeout()
}