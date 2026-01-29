package br.com.dev360.gemisnap.feature.snap.hub.domain

import android.net.Uri
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiResponse

interface HubSnapContract {
    interface RemoteDataSource {
        suspend fun generateContent(
            apiKey: String,
            request: GeminiRequest
        ): GeminiResponse
    }

    interface Repository {
        suspend fun generateContent(
            prompt: String,
            base64Image: String,
        ): String?
    }

    interface UiModel {
        fun getDefaultPrompt(prompt: String): String
        fun decodeAndConvertBase64(uri: Uri): String
    }
}