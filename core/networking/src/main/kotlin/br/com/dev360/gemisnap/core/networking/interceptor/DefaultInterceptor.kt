package br.com.dev360.gemisnap.core.networking.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DefaultInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return proceedWithRequest(request, chain)
    }
}

private fun proceedWithRequest(request: Request, chain: Interceptor.Chain): Response {

    val newRequest = request.newBuilder()
        .addHeader("Content-Type", "application/json")
        .build()

    return chain.proceed(newRequest)
}