package br.com.dev360.gemisnap.core.networking.di

import br.com.dev360.gemisnap.core.networking.di.qualifiers.QualifierGeminiAiHost
import br.com.dev360.gemisnap.core.networking.factory.NetworkResponseAdapterFactory
import br.com.dev360.gemisnap.core.networking.util.GsonProvider
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://generativelanguage.googleapis.com/"

val RetrofitModule = module {
    single(QualifierGeminiAiHost) {
        provideRetrofit(
            okHttpClient = get(),
            url = get(QualifierGeminiAiHost)
        )
    }

    single(QualifierGeminiAiHost) { BASE_URL }
}

private fun provideRetrofit(
    okHttpClient: OkHttpClient.Builder,
    url: String
) : Retrofit =
    Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create(GsonProvider.get()))
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .build()