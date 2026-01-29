package br.com.dev360.gemisnap.feature.snap.hub.domain

import android.net.Uri
import br.com.dev360.gemisnap.core.networking.data.ResultWrapper
import br.com.dev360.gemisnap.core.networking.exception.Failure
import br.com.dev360.gemisnap.core.networking.factory.NetworkResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiResponse

interface HubSnapContract {
    interface RemoteDataSource {
        suspend fun generateContent(
            apiKey: String,
            request: GeminiRequest
        ): NetworkResponse<GeminiResponse>
    }

    interface Repository {
        suspend fun generateContent(
            prompt: String,
            base64Image: String,
        ): ResultWrapper<String?>
    }

    interface UiModel {
        fun getDefaultPrompt(prompt: String): String
        fun decodeAndConvertBase64(uri: Uri): String
        fun getErrorMessage(failure: Failure): String
        fun getDecodeImageErrorMessage(): String
    }
}