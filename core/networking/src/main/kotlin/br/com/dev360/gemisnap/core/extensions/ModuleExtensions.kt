package br.com.dev360.gemisnap.core.extensions

import br.com.dev360.gemisnap.core.networking.di.qualifiers.QualifierGeminiAiHost
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import retrofit2.Retrofit

inline fun <reified T> Scope.provideRetrofitApi(qualifier: Qualifier? = QualifierGeminiAiHost): T =
    get<Retrofit>(qualifier).create(T::class.java)