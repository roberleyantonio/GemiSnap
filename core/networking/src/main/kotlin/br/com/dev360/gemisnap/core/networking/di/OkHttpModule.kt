package br.com.dev360.gemisnap.core.networking.di

import br.com.dev360.gemisnap.core.networking.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

internal val okHttpModule = module {
    single {
        provideOkHttpClientBuilder(
            interceptor = get()
        )
    }
}

private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}

private fun provideOkHttpClientBuilder(
    interceptor: Interceptor
): OkHttpClient.Builder {
    val okHttpClientBuilder = OkHttpClient.Builder()
    okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
    okHttpClientBuilder.addInterceptor(interceptor)

    return okHttpClientBuilder
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
}