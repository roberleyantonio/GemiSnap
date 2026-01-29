package br.com.dev360.gemisnap.feature.snap.hub.data

import br.com.dev360.gemisnap.feature.snap.BuildConfig
import br.com.dev360.gemisnap.feature.snap.hub.data.model.ContentRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.InlineData
import br.com.dev360.gemisnap.feature.snap.hub.data.model.PartRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.toText
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract

class HubSnapRepositoryImpl(
    private val remoteDataSource: HubSnapContract.RemoteDataSource
) : HubSnapContract.Repository {
    override suspend fun generateContent(prompt: String, base64Image: String): String? {
        val request = GeminiRequest(
            contents = listOf(
                ContentRequest(
                    parts = listOf(
                        PartRequest(text = prompt),
                        PartRequest(
                            inlineData = InlineData(
                                mimeType = MIME_TYPE,
                                data = base64Image
                            )
                        )
                    )
                )
            )
        )

        return remoteDataSource.generateContent(
            apiKey = BuildConfig.GEMINI_API_KEY,
            request = request
        ).toText()
    }

    private companion object {
        const val MIME_TYPE = "image/jpeg"
    }

}