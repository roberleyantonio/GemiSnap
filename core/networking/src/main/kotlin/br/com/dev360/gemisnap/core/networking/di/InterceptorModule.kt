package br.com.dev360.gemisnap.core.networking.di

import br.com.dev360.gemisnap.core.networking.interceptor.DefaultInterceptor
import okhttp3.Interceptor
import org.koin.dsl.module

internal val interceptorModule = module {
    single<Interceptor> {
        DefaultInterceptor()
    }
}