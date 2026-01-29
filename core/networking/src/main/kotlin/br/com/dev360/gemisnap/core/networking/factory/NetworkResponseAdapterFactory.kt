package br.com.dev360.gemisnap.core.networking.factory

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.CallAdapter
import retrofit2.Retrofit
import kotlin.jvm.java

class NetworkResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return try {
            check(returnType is ParameterizedType) {
                "return type"
            }

            val responseType = getParameterUpperBound(0, returnType)

            if (getRawType(responseType) != NetworkResponse::class.java) {
                return null
            }

            check(responseType is ParameterizedType) { "Response must be parameterized" }

            val successBodyType = getParameterUpperBound(0, responseType)

            return NetworkResponseAdapter(successBodyType)

        } catch (ex: ClassCastException) {
            null
        }
    }
}