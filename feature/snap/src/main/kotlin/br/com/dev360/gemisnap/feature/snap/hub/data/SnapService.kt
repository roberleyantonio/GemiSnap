package br.com.dev360.gemisnap.feature.snap.hub.data

import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface SnapService {
    @POST(GENERATE_CONTENT_V1)
    suspend fun generateContent(
        @Query(KEY_FIELD) apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse


    private companion object {
        const val GENERATE_CONTENT_V1 = "v1/models/gemini-2.5-flash:generateContent"
        const val KEY_FIELD = "key"
    }
}